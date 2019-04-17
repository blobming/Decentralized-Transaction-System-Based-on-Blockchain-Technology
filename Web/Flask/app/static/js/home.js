
let url = "http://localhost:8080/api/dataarr";
let jsonData = getData(url,function(text){
    let data = JSON.parse(text);
    console.log(data);
    appendBlock(data);

});

function getData(url, callback){
    var ajaxReq = new XMLHttpRequest();
    ajaxReq.overrideMimeType("application/json");
    ajaxReq.open('GET',url);
    ajaxReq.send();
    ajaxReq.onreadystatechange = function(){
        if(ajaxReq.readyState == 4 && ajaxReq.status == "200"){
            callback(ajaxReq.responseText);
        }
    }
}

function appendBlock(arr){
    let table = document.getElementById('blockTableBody');
    let url = "../public/detail.html?blockid="
    console.log(table);
    for(let i=0;i<(arr.length<=10?arr.length:10);i++){
        let trow = document.createElement('tr');
        let height = createTd();
        let heightLink = createALink(arr[i].block.serialVersionUID, url+arr[i].block.serialVersionUID);
        height.appendChild(heightLink);
        trow.appendChild(height);
        
        let serverName = createTd();
        let serverNameP = createAPrograph('Sup Bank');
        serverName.appendChild(serverNameP);
        trow.appendChild(serverName);


        let timeStamp = createTd();
        let timeStampP = createAPrograph(arr[i].block.timeStamp);
        timeStamp.appendChild(timeStampP);
        trow.appendChild(timeStamp);


        let hashCode = createTd();
        let hashCodeLink = createALink(arr[i].block.hashCode);
        hashCode.appendChild(hashCodeLink);
        trow.appendChild(hashCode);
        table.appendChild(trow);
    }
}



function createALink(value, url){
    let ALink = document.createElement('a');
    ALink.href = url;
    ALink.textContent = value;
    return ALink;
}

function createAPrograph(text){
    let prograph = document.createElement('p');
    prograph.textContent = text;
    return prograph;
}

function createTd(){
    return document.createElement('td');
}





