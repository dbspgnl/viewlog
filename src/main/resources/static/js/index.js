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
		// data: { bundleIndex: index },
		success: function (result) {
			console.log(result);
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});  

}