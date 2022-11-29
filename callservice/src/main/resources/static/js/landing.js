const entry = document.getElementById('userkey');
const loginbtn = document.getElementById('loginbtn');

loginbtn.onclick = handleSubmit;

//Using plain HTTP GET reveals our sensitive data;
//->Workaround: use HTTP POST; emmited data hidden
function handleSubmit(event)
{
    event.preventDefault();
    
    var form = document.createElement('form');
    document.body.appendChild(form);
    form.method = 'post';
    form.action = "http://localhost:8080/home";    //modify on switch between testing and deployment
    
    var input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'key';
    input.value = entry.value;
    form.appendChild(input);
    
    form.submit();
    document.body.removeChild(form);
}