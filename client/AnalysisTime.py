class MethodTraceResult:

    def __init__(self, base_time, child):
        # 耗时
        self.base_time = base_time
        # 类名
        self.child = child


class LineTraceResult:

    def __init__(self, name, time):
        # 耗时
        self.time = time
        # 类名
        self.name = name


def get_median(data):
    half = len(data) // 2
    return (data[half] + data[~half]) / 2


file_path = '/Users/zhangyunfan/Desktop/com.fiture.content.taurus.training.resultpage.service.impl.TrainingResultsPageServiceImpl#getResultsV3.txt'

list = []
base_time = ''
child_time = []
for line in open(file_path, encoding=
'utf-8'):
    if line.startswith('['):
        base_time = line.split(' ')[0].replace('[', '').replace(']', '')
    if line.startswith('     +['):
        child_time.append(
            LineTraceResult(line.split(' ')[7] + line.split(' ')[8], float(line.split(' ')[5].replace('+[', ''))))
    if line.startswith('线程名是'):
        if len(child_time) >= 1:
            list.append(MethodTraceResult(float(base_time), child_time))
            base_time = ''
            child_time = []

sort_list = sorted(list, key=lambda x: x.base_time, reverse=True)
statistics_map = {}
statistics_time_map = {}
for method in sort_list:
    method.child = sorted(method.child, key=lambda x: x.time, reverse=True)
    if method.child[0].name in statistics_map:
        statistics_map[method.child[0].name] = statistics_map[method.child[0].name] + 1
        proportion = (method.child[0].time / method.base_time) * 100
        statistics_time_map[method.child[0].name].append(proportion)
    else:
        statistics_map[method.child[0].name] = 1
        statistics_time_map[method.child[0].name] = []
        proportion = (method.child[0].time / method.base_time) * 100
        statistics_time_map[method.child[0].name].append(proportion)

print('一共捕获了{}次运行'.format(len(list)))
for method in statistics_map.keys():
    print('方法{}是最慢的方法次数{}次，最慢的时候占总时间比例的中位数为{}'.format(method, statistics_map[method],
                                                      str(get_median(statistics_time_map[method])) + '%'))
