let perTime = 60000; // 1λΆ

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
				$("#isRemotingSpan").text("π΄"+result.host+" μκ²©μ€...");
			} else if(result == null) {
				$("#isRemotingSpan").text("βμλ² μ μλΆκ°");
			} else if(!result.result && result.host != "127.0.0.1"){
				$("#isRemotingSpan").text("π’"+result.host+" μ¬μ©κ°λ₯");
			} else {
				console.log(result);
				$("#isRemotingSpan").text("λκΈ°μ€...");
			}
			btnToggle();
		},
		error: function (e) {
			console.log(e.responseText);
			$("#isRemotingSpan").text("βμλ² μ μλΆκ°");
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
				const logPath = element.logPath && element.logPath.replaceAll("\\", "\\\\");
				$("#serverListTbody").append(`
					<tr>
						<td>${element.status == 1 ? 'π’':'π΄'}</td>
						<td>${element.name}</td>
						<td>${new Date(element.date).toLocaleString()}</td>
						<td> 
							<button class="btn btn-secondary" onclick="getConsoleLog(${element.index}, '${element.name}')">μΆμ </button>
							<button type="button" class="btn btn-secondary" onClick="openPutModal(${element.index}, '${element.name}', '${logPath}', '${element.port}')">μ€μ </button>
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
			intervalStr = "10μ΄"
			break;
		case 30000:
			intervalStr = "30μ΄"
			break;
		case 60000:
			intervalStr = "1λΆ"
			break;
		case 180000:
			intervalStr = "3λΆ"
			break;
		case 300000:
			intervalStr = "5λΆ"
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
			if(result == "μ€ν κ²½λ‘λ₯Ό μ°Ύμ μ μμ΅λλ€.") {
				alert(result);
			} else {
				alert(result +" μλ²λ₯Ό μ¬μμ νμ΅λλ€.");
			}
			doReload();
		},
		error: function (e) {
			console.log(e.responseText);
		}
	}); 
}