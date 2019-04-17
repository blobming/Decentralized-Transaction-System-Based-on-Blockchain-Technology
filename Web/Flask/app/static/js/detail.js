(function(){
    var thisURL = document.URL;
    var blockIdStr = thisURL.split('?'); 
    if(blockIdStr[1]){
        blockIdStr = blockIdStr[1];
    }
    var blockId = blockIdStr.split("=")[1];  //通过传过来的url进行解析获得blockid


    let url = "http://localhost:8080/api/singledata";
    // url += ("?"+blockIdStr); 通过GET向服务器提供目标块ID
    let jsonData = getData(url,function(text){
        let data = JSON.parse(text);
        let transactions = data.block.BlockBody.transactions;
        appendBlockDetails(data);
        appendBlockTransactions(data.block.BlockBody.transactions);
    });
})();


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

function appendBlockDetails(data){
    let leftDl = document.getElementById('leftDl');
    let rightDl = document.getElementById("rightDl");

    let nonce = createARow("Nonce", data.block.nonce);
    let height = createARow("Height", data.block.serialVersionUID);
    let bits = createARow("Bits", data.block.nBits);
    let timeStamp = createARow("Time", data.block.timeStamp);
    let blockHash = createARow("Block Hash", data.block.hashCode);
    let preBlockHash = createARow("Pre Block Hash", data.block.preHashCode);
    let merkleRootHash = createARow("Merkle Root Hash", data.block.merkleRootHash);

    appendRow(height, leftDl);
    appendRow(bits, leftDl);
    appendRow(nonce, leftDl);
    appendRow(timeStamp, leftDl);
    appendRow(blockHash, rightDl);
    appendRow(preBlockHash, rightDl);
    appendRow(merkleRootHash, rightDl);

}

function appendBlockTransactions(transactions){
    let transactionsDiv = document.getElementById('transactionsDiv');
    let url = '#';
    for(let i=0;i<transactions.length;i++){
        let trans = createTransaction(transactions[i].txid, transactions[i].hash, transactions[i].timeStamp, url, transactions[i]);
        transactionsDiv.appendChild(trans);
    }
}

function createTransaction(txid, hashcode, timestamp, url, transaction){
    let transactionDiv = createElement('div');

    transactionDiv.classList.add('transaction-title', 'row');
    let idDiv = createElement('div');
    idDiv.classList.add('col-md-2');
    let idLink = createElement('a');
    idLink.href = url;
    let id = createAPrograph(txid);
    idLink.appendChild(id);
    idDiv.appendChild(idLink);


    let hashDiv = createElement('div');
    hashDiv.classList.add('col-md-5');
    let hash = createAPrograph(hashcode);
    hashDiv.appendChild(hash);

    let timeDiv = createElement('div');
    timeDiv.classList.add('col-md-3');
    let time = createAPrograph(timestamp);
    timeDiv.appendChild(time);
    
    let detailBtn = createElement('button');
    detailBtn.textContent = 'detail';
    detailBtn.classList.add('btn', 'btn-primary')
    detailBtn.style ='margin-top: 2px;';
    detailBtn.setAttribute('data-toggle', 'modal');
    detailBtn.setAttribute('data-target', '#myModal'+transaction.txid);
    
    let modal = createModal(transaction);

    transactionDiv.appendChild(idDiv);
    transactionDiv.appendChild(hashDiv);
    transactionDiv.appendChild(timeDiv);
    transactionDiv.appendChild(detailBtn);
    transactionDiv.appendChild(modal);

    return transactionDiv;
}

function createModal(transaction){


    let modalDiv = createDivWithClass('modal', 'fade');
    let modalDialogDiv = createDivWithClass('modal-dialog');
    let modalContentDiv = createDivWithClass('modal-content');
    let modalHeaderDiv = createDivWithClass('modal-header');
    let modalBodyDiv = createDivWithClass('modal-body');
    let modalFooterDiv = createDivWithClass('modal-footer');
    //header content
    let close = createElement('button');
    close.classList.add('close');
    close.textContent = "x";
    close.setAttribute("data-dismiss","modal");
    close.setAttribute("aria-hidden","true");

    let title = createElement('h4');
    title.classList.add('modal-title');
    // title.textContent = 'Transaction: ';
    title.textContent = 'Transaction: ' + transaction.txid;
    modalHeaderDiv.appendChild(close);
    modalHeaderDiv.appendChild(title);
    modalContentDiv.appendChild(modalHeaderDiv);
    //body content

    let time = createAPrograph(transaction.timeStamp);
    let hash = createAPrograph(transaction.hash);
    let coinBase = transaction.isCoinBase == 'true'?createAPrograph('CoinBase'):createAPrograph('');

    let vins = createVins(transaction.Vin);
    let vouts = createVouts(transaction.Vout);

    modalBodyDiv.appendChild(time);
    modalBodyDiv.appendChild(hash);
    modalBodyDiv.appendChild(coinBase);
    modalBodyDiv.appendChild(vins);
    modalBodyDiv.appendChild(vouts);
    modalContentDiv.appendChild(modalBodyDiv);

    //footer content
    let footerClose = createElement('button');
    footerClose.classList.add('btn', 'btn-default');
    footerClose.setAttribute('data-dismiss', 'modal');

    footerClose.textContent = 'OK';
    modalFooterDiv.appendChild(footerClose);
    modalContentDiv.appendChild(modalFooterDiv);


    //mount
    modalDialogDiv.appendChild(modalContentDiv);
    modalDiv.appendChild(modalDialogDiv);
    modalDiv.setAttribute('id', 'myModal'+transaction.txid);
    modalDiv.setAttribute('role', 'dialog');
    modalDiv.setAttribute('aria-hidden', 'true');

    return modalDiv;
}

function createDivWithClass(v1,v2){
    let div = createElement('div');
    div.classList.add(v1);
    if(v2){
        div.classList.add(v2);
    }
    return div;
}

function createARow(key, value){
    let keyDt = createElement('dt');
    keyDt.appendChild(createAPrograph(key));
    let valueDd = createElement('dd');
    valueDd.appendChild(createAPrograph(value));
    
    return [keyDt, valueDd];
        
}

function appendRow(arr, fatherElement){
    arr.forEach(element => {
        fatherElement.appendChild(element);
    });
}

function createAPrograph(text){
    let prograph = document.createElement('p');
    prograph.textContent = text;
    return prograph;
}

function createElement(tag){
    return document.createElement(tag);
}

function addVin(vin){
    let vinDiv = createElement('div');
    let vinDl = createElement('dl');
    let title = createAPrograph('Vin');
    vinDl.classList.add("dl-horizontal");

    let txid = createARow('Transaction id: ', vin.txid);
    let voutNum = createARow('Vout Number: ', vin.voutNum);
    let signature = createARow('Signature: ', vin.signature);
    let publickey = createARow('Public Key: ', vin.publickey);

    appendRow(txid, vinDl);
    appendRow(voutNum, vinDl);
    appendRow(signature, vinDl);
    appendRow(publickey, vinDl);

    vinDiv.appendChild(title);
    vinDiv.appendChild(vinDl);

    return vinDiv;
    
}

function createVins(vins){
    let vinsDiv = createElement('div');
    for(let i=0;i<vins.length;i++){
        vinsDiv.appendChild(addVin(vins[i]));
    }
    return vinsDiv;
}

function addVout(vout){
    let voutDiv = createElement('div');
    let title = createAPrograph('Vout');
    let voutDl = createElement('dl');
    voutDl.classList.add("dl-horizontal");
    let coins = createARow('Coins: ', vout.value);
    let seqNm = createARow('Number: ', vout.seqNm);
    let pubHash = createARow('pubHash: ', vout.pubHash);
    
    appendRow(coins, voutDl);
    appendRow(seqNm, voutDl);
    appendRow(pubHash, voutDl);

    voutDiv.appendChild(title);
    voutDiv.appendChild(voutDl);

    return voutDiv;
    
}

function createVouts(vouts){
    let voutsDiv = createElement('div');
    for(let i=0;i<vouts.length;i++){
        voutsDiv.appendChild(addVout(vouts[i]));
    }
    return voutsDiv;
}


