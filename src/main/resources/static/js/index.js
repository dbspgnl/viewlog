intervalFunction();
setInterval(() => intervalFunction(),60000); // 1분마다 체크

function intervalFunction() {
	isRemoting();
}

function isRemoting() {
	$.ajax({
		url: "/isRemoting",
		type: "GET",
		success: function (result) {
			if(result){
				$("#isRemotingSpan").text("🔴108 서버 사용중...");
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

function getServerList() {
	$.ajax({
		url: "/getServerList",
		type: "GET",
		success: function (result) {
			console.log(result);
			$("tbody").empty();
			result.forEach(element => {
				$("tbody").append(`
					<tr>
						<td>${element.start == 1 ? '🟢':'🔴'}</td>
						<td>${element.name}</td>
						<td>${new Date(element.date).toLocaleString()}</td>
						<td> <button class="btn btn-primary">새로고침</button></td>
						<td> 
							<button class="btn btn-secondary">시작</button>
							<button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#putModal">설정</button>
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