function check(){
    let email = document.getElementById("email");
    let passwd = document.getElementById("password");
    if(email.value){
        if(isEmail(email.value)){
            if(passwd.value){
                if(passwd.value.length < 9){
                    alert('week password (should be more than 8 word)')
                    return false;
                }
            }else{
                alert('password cannot be empty.');
                return false;
            }
        }else{
            alert('not a email address.');
            return false;
        }
    }else{
        alert('email cannot be empty.');
        return false;
    }
}

function isEmail(email){
    console.log(email)
    if(email==null){
        return false;
    }

    let reg=new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
    return reg.test(email);
}
