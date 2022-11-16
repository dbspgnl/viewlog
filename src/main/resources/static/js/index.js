let perTime = 60000; // 1분

intervalFunction();
doInterval();

function doInterval() {
	setInterval(() => intervalFunction(), perTime);
}

function intervalFunction() {
	isRunServer();
}

function doReload(){
	btnToggle();
	intervalFunction();
}

function btnToggle() {
	$("#btn-running").toggle();
	$("#btn-reload").toggle();
}

function isRunServer() { 
	$.ajax({
		url: "/isRunServer",
		type: "GET",
		success: function (result) {
			getServerList();
			isRemoting();
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}

function isRemoting() {
	$.ajax({
		url: "/isRemoting",
		type: "GET",
		success: function (result) {
			if(result.result && result.host != "127.0.0.1"){
				$("#isRemotingSpan").text("🔴"+result.host+" 원격중...");
			} else if(result == null) {
				$("#isRemotingSpan").text("❌서버 접속불가");
			} else if(!result.result && result.host != "127.0.0.1"){
				$("#isRemotingSpan").text("🟢"+result.host+" 사용가능");
			} else {
				console.log(result);
				$("#isRemotingSpan").text("대기중...");
			}
			btnToggle();
		},
		error: function (e) {
			console.log(e.responseText);
			$("#isRemotingSpan").text("❌서버 접속불가");
			btnToggle();
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
				$("#serverListTbody").append(`
					<tr>
						<td>${element.status == 1 ? '🟢':'🔴'}</td>
						<td>${element.name}</td>
						<td>${new Date(element.date).toLocaleString()}</td>
						<td> 
							<button class="btn btn-secondary" onclick="getConsoleLog(${element.index}, '${element.name}')">추적</button>
							<button type="button" class="btn btn-secondary" onClick="openPutModal(${element.index}, '${element.name}', '${element.logPath}', '${element.port}', '${element.startPath}')">설정</button>
							<button class="btn btn-secondary" onclick="startUp('${element.startPath}')">재시작</button>
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

function openPutModal(index, name, logPath, port, startPath) {
	$("#putInput0").val(index);
	$("#putInput1").val(name);
	$("#putInput2").val(logPath);
	$("#putInput3").val(port);
	$("#putInput4").val(startPath);
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

function startUp(startPath) {
	let formData = new FormData();
	formData.append("startPath", startPath);
	$.ajax({
		url: "/startUp",
		type: "POST",
		data: formData,
		processData: false,
		contentType: false,
		success: function (result) {
			if(result == "실행 경로를 찾을 수 없습니다.") {
				alert(result);
			} else {
				alert(result +" 서버를 재시작 했습니다.");
			}
			doReload();
		},
		error: function (e) {
			console.log(e.responseText);
		}
	}); 
}