intervalFunction();
setInterval(() => intervalFunction(),60000); // 1분마다 체크

function intervalFunction() {
	isRemoting();
}

function addServer() {
	alert("등록 아직 미구현")
}

function isRemoting() {
	$.ajax({
		url: "/isRemoting",
		type: "GET",
		success: function (result) {
			if(result){
				$("#isRemotingSpan").text("🟠108 서버 사용중...");
			}
			else{
				$("#isRemotingSpan").text("🟢108 서버 사용가능");
			}
		},
		error: function (e) {
			console.log(e.responseText);
			$("#isRemotingSpan").text("🔴108 서버 접속불가");
		}
	});  
}

function ssh() {
	$.ajax({
		url: "/ssh",
		type: "GET",
		success: function (result) {
			console.log(result);
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}

function log() {
	$.ajax({
		url: "/log",
		type: "GET",
		success: function (result) {
			result.forEach(element => {
				$("#console_container").append(element);
				$("#console_container").append('<br>');
			});
			let consoleUl = document.querySelector('#console_container');
			consoleUl.scrollTop = consoleUl.scrollHeight;
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  
}