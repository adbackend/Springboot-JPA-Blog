let index = {
	init: function(){
		
		$("#btn-save").on("click",()=>{ // function(){}, ()=> this를 바인딩하기 위해서!!
			this.save();
		});
		$("#btn-delete").on("click",()=>{ // function(){}, ()=> this를 바인딩하기 위해서!!
			this.deleteById();
		});
		
	},
	
	save : function(){
//		alert('user의 save 함수 호출됨');
	
		let data = {
			title : $("#title").val(),
			content : $("#content").val()
		};
		$.ajax({
			
			type :"post",
			url : "/api/board",
			data : JSON.stringify(data), //http body 데이터
			contentType : "application/json; charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
			dataType : "json" // 요청을 서버로해서 응답이 왔을때 기본적으로 모든것이 문자열(생긴게 json이라면->javascript 오브젝트로 변경)
			
		}).done(function(resp){
			
			alert("글쓰기가 완료되었습니다.");
			console.log(resp);
			location.href="/";
			
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},

	deleteById : function(){
		
		var id = $("#id").text();
		alert("id오냐...?"+id);
		
		$.ajax({
			
			type :"delete",
			url : "/api/board/"+id,
			dataType:"json"	,
			contentType:"application/json; charset=utf-8"		
		}).done(function(resp){
			
			location.href="/";
			
		}).fail(function(error){
			
			alert("삭제실패"+JSON.stringify(error));
		}); 
	}
}


index.init();