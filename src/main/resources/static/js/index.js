intervalFunction();
let perTime = 60000; // 1분
doInterval();

function doInterval() {
	setInterval(() => intervalFunction(),perTime);
}

function intervalFunction() {
	isRemoting();
	isRunServer();
}

function isRemoting() {
	$.ajax({
		url: "/isRemoting",
		type: "GET",
		success: function (result) {
			if(result){
				$("#isRemotingSpan").text("🔴108 서버 사용중...");
			} else if(result == null) {
				$("#isRemotingSpan").text("❌108 서버 접속불가");
			}
			else{
				$("#isRemotingSpan").text("🟢108 서버 사용가능");
			}
		},
		error: function (e) {
			console.log(e.responseText);
			$("#isRemotingSpan").text("❌108 서버 접속불가");
		}
	});  
}

function getConsoleLog(index, name) {
	$.ajax({
		url: "/getConsoleLog/"+index,
		type: "GET",
		success: function (result) {
			result.forEach(element => {
				$("#console_container").append(element);
				$("#console_container").append('<br>');
			});
			let consoleUl = document.querySelector('#console_container');
			consoleUl.scrollTop = consoleUl.scrollHeight;
			$("#console_title").text(name);
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}

function getServerList() {
	$.ajax({
		url: "/getServerList",
		type: "GET",
		success: function (result) {
			$("#serverListTbody").empty();
			result.forEach(element => {
				const server = JSON.stringify(element).toString();
				$("#serverListTbody").append(`
					<tr>
						<td>${element.status == 1 ? '🟢':'🔴'}</td>
						<td>${element.name}</td>
						<td>${new Date(element.date).toLocaleString()}</td>
						<td> 
							<button class="btn btn-secondary" onclick="getConsoleLog(${element.index}, '${element.name}')">시작</button>
							<button type="button" class="btn btn-secondary" onClick="openPutModal(${element.index}, '${element.name}', '${element.logPath}', '${element.port}')">설정</button>
						</td>
					</tr>
				`);
			});
			
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}

function isRunServer() { 
	$.ajax({
		url: "/isRunServer",
		type: "GET",
		success: function (result) {
			getServerList();
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}

function openPutModal(index, name, logPath, port) {
	$("#putInput0").val(index);
	$("#putInput1").val(name);
	$("#putInput2").val(logPath);
	$("#putInput3").val(port);
	$("#putModal").modal("show");
}

function changeInterval(time) {
	perTime = time;
	doInterval();
	let intervalStr = "";
	switch (time) {
		case 10000:
			intervalStr = "10초"
			break;
		case 30000:
			intervalStr = "30초"
			break;
		case 60000:
			intervalStr = "1분"
			break;
		case 180000:
			intervalStr = "3분"
			break;
		case 300000:
			intervalStr = "5분"
			break;
		default:
			break;
	}
	$("#intervalView").text(intervalStr);
	$("#intervalModal").modal("hide");
}