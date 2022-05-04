from urllib import request, parse
import json
import time

host = ''
method = ''
result_path = ''

headers = {
    'Content-Type': 'application/json',
    'User-Agent': 'PostmanRuntime/7.28.4',
    'Accept': '*/*',
    'Connection': 'keep-alive'
}


class LineResult:

    def __init__(self, cost, className, methodName, lineNumber, type):
        # 耗时
        self.cost = cost
        # 类名
        self.className = className
        # 方法名
        self.methodName = methodName
        # 行数
        self.lineNumber = lineNumber
        # 类型
        self.type = type

    def __str__(self):
        if self.lineNumber is not -1:
            return '[{} ms] {}#{} lineNumber:{}'.format(self.cost / 1000000, self.className, self.methodName,
                                                        self.lineNumber)
        else:
            return '[{} ms] {}#{}'.format(self.cost / 1000000, self.className, self.methodName)


def beginTrace(methodAddress):
    print('开始异步执行trace，要观测的方法是：' + methodAddress)
    data = {'methodAddress': methodAddress}
    req = request.Request(host + '/arthas/async/trace', data=json.dumps(data).encode(), headers=headers, method='POST')
    newsResponse = json.loads(request.urlopen(req).read().decode('utf-8'))
    print('本次trace的会话信息为{}'.format(newsResponse))
    return newsResponse


def pullTrance(sessionId, consumerId):
    req = request.Request(host + '/arthas/pull/{}/{}'.format(sessionId, consumerId), method='GET')
    newsResponse = json.loads(request.urlopen(req).read().decode('utf-8'))
    return newsResponse


def getClassTrace(children, result):
    # 目标类的每一行的信息
    for childNode in children['children']:
        childrenClass = LineResult(childNode['cost'], childNode['className'], childNode['methodName'],
                                   childNode['lineNumber'],
                                   childNode['type'])
        result.append(childrenClass)
        if 'children' in childNode and len(childNode['children']) >= 1:
            getClassTrace(childNode, result)
        else:
            continue


def analysisResult(root, result_file_path):
    threadName = root['threadName']
    timestamp = root['timestamp']
    if len(root['children']) < 1:
        return
    result = []
    baseClass = LineResult(root['children'][0]['cost'], root['children'][0]['className'],
                           root['children'][0]['methodName'],
                           root['children'][0]['lineNumber'],
                           root['children'][0]['type'])
    result.append(baseClass)
    getClassTrace(root['children'][0], result)
    with open(result_file_path, 'a') as f:
        f.write('线程名是{},执行时间{}'.format(threadName, timestamp) + '\n')
        for line in result:
            if line.lineNumber is -1:
                f.write(LineResult.__str__(line) + '\n')
            else:
                f.write('     +{}'.format(line) + '\n')
        f.write('\n')
        f.write('\n')
        f.write('\n')
        f.write('\n')
        f.write('\n')


result = beginTrace(
    method)
jobId = result['body']['jobId']
result_file_path = result_path + '/' + method.replace(' ', '#') + '.txt'
i = 1
while True:
    traceResult = pullTrance(result['sessionId'], result['consumerId'])
    results = json.loads(traceResult['body']['results'])
    if len(results) >= 1:
        for trace in results:
            if 'jobId' in trace and trace['jobId'] == jobId and 'root' in trace:
                print('第{}次执行，开始写入结果到文件{}里'.format(str(i), result_file_path))
                root = trace['root']
                analysisResult(root, result_file_path)
                i = i + 1
    time.sleep(1)
