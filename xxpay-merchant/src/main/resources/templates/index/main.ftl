<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" href="plugins/layui-v2.4.5/css/layui.css" media="all" />
		<link rel="stylesheet" href="css/main.css" />
	</head>

	<body>
		<div class="admin-main">
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>交易额</legend>
			</fieldset>

			<div style="padding: 20px; background-color: #F2F2F2;">
				<div class="layui-row layui-col-space15">
					<div class="layui-col-md3 layui-col-md-offset1">
						<div class="layui-card">
							<div class="layui-card-header">今日交易额：<span style="color:red;">${todayAmount}</span> 元</div>
							<div class="layui-card-body">
								总交易金额：<span style="color:red;">${totalAmount} </span> 元
							</div>
						</div>
					</div>
					<div class="layui-col-md3 layui-col-md-offset1">
						<div class="layui-card">
							<div class="layui-card-header">今日单数：<span style="color:red;">${todayCount}</span> 单</div>
							<div class="layui-card-body">
								总单数：<span style="color:red;">${totalCount}</span> 单
							</div>
						</div>
					</div>
					<div class="layui-col-md3 layui-col-md-offset1">
						<div class="layui-card">
							<div class="layui-card-header">今日未付订单数：<span style="color:red;">${todayNotSuccessCount}</span> 单  空单率：<span style="color:red;">${todayNotSuccessRate}</span>%</div>
							<div class="layui-card-body">
								未付总单数：<span style="color:red;">${totalNotSuccessCount}</span> 单 空单率：<span style="color:red;">${totalNotSuccessRate}</span> %
							</div>
						</div>
					</div>


				</div>

			</div>

			<div class="layui-col-md12">
				<div class="layui-card">
					<div class="layui-card-header">日交易统计</span>
					</div>
					<div class="layui-card-body">
						<div id="main" style="width: 100%;height:400px; "></div>
					</div>
				</div>
			</div>
		</div>
		<script src="https://cdn.bootcss.com/echarts/4.2.1/echarts.min.js"></script>
		<script src="https://cdn.bootcss.com/jquery/3.3.0/jquery.min.js"></script>
		<script type="text/javascript">
			// 基于准备好的dom，初始化echarts实例
			var myChart = echarts.init(document.getElementById('main'));

			// 指定图表的配置项和数据
			var option = {
				title: {
					text: '交易订单统计'
				},
				tooltip: {
					trigger: 'axis',
					axisPointer: {
						type: 'cross',
						label: {
							backgroundColor: '#6a7985'
						}
					}
				},
				legend: {
					data: ['订单','成交']
				},
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [
					{
						type: 'category',
						boundaryGap: false,
						// data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
					}
				],
				yAxis: [
					{
						type: 'value'
					}
				],
				series: [
					// {
					//     name: '成交',
					//     type: 'line',
					//     stack: '总量',
					//     areaStyle: {normal: {}},
					//     data: [320, 332, 301, 334, 390, 330, 320]
					// },
					// {
					//     name: '订单',
					//     type: 'line',
					//     stack: '总量',
					//     label: {
					//         normal: {
					//             show: true,
					//             position: 'top'
					//         }
					//     },
					//     areaStyle: {normal: {}},
					//     data: [820, 932, 901, 934, 1290, 1330, 1320]
					// }
				]
			};

			// 使用刚指定的配置项和数据显示图表。
			$.get("/query_pay_order_statistics", function (res) {
				option.xAxis[0].data = res["xAxis.data"];
				var successSeries = {
					name: '成交',
					type: 'line',
					stack: '总量',
					areaStyle: {normal: {}},
					data: res["series.data"]["success"]
				};

				option.series.push(successSeries);
				var totalSeries = {
					name: '订单',
					type: 'line',
					stack: '总量',
					label: {
						normal: {
							show: true,
							position: 'top'
						}
					},
					areaStyle: {normal: {}},
					data: res["series.data"]["total"]
				};
				option.series.push(totalSeries);
				myChart.setOption(option);
			}, 'JSON');

			// setTimeout(function () {
			//     location.reload();
			// },10000);
		</script>
	</body>

</html>