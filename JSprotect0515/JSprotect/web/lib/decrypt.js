function nisl2(argu01,argu02){
    var result=argu01+argu02;
    return result;
}

function nisl4(argu1,argu2,argu3,argu4){
    var result=argu1+argu2;
    result+=nisl2(argu3,argu4);
    return result;
}

function nisl6(argu1,argu2,argu3,argu4,argu5,argu6){
    var result=nisl2(argu1,argu2,argu5)+nisl4(argu3,argu4,argu5,argu6);
    return result;
}

