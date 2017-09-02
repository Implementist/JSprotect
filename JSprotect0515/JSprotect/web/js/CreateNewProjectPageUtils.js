/**
 * Created by Implementist on 2017/6/23.
 */

function setDisabledOfComputationParam() {
    if (document.getElementsByName("chbComputation")[0].value === "unchecked") {
        document.getElementsByName("txtComputationParam")[0].removeAttribute("readonly");
        document.getElementsByName("chbComputation")[0].value = "checked";
    } else {
        document.getElementsByName("txtComputationParam")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("chbComputation")[0].value = "unchecked";
    }
}

function setDisabledOfFlattenParams() {
    if (document.getElementsByName("chbControlFlowFlatten")[0].value === "unchecked") {
        document.getElementsByName("txtThresholdValue")[0].removeAttribute("readonly");
        document.getElementsByName("txtBlockSize")[0].removeAttribute("readonly");
        document.getElementsByName("chbControlFlowFlatten")[0].value = "checked";
    } else {
        document.getElementsByName("txtThresholdValue")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("txtBlockSize")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("chbControlFlowFlatten")[0].value = "unchecked";
    }
}

function reverseValue(element) {
    if (element.value === "unchecked")
        element.value = "checked";
    else
        element.value = "unchecked";
}

function reverseChecked(element) {
    if (element.getAttribute('unchecked') === "unchecked")
        element.setAttribute('unchecked', 'checked');
    else
        element.setAttribute('unchecked', 'unchecked');
}

function changeMaxValue() {
    document.getElementsByName('txtBlockSize')[0].max = document.getElementsByName('txtThresholdValue')[0].value - 1;
}

function addACheckBoxWithOneText(parent, value) {
    var div1 = document.createElement('div');
    div1.setAttribute('class', 'field');

    var div2 = document.createElement('div');
    div2.setAttribute('class', 'ui checked checkbox');

    var label = document.createElement('label');

    var node = document.createElement('input');
    node.setAttribute('type', 'checkbox');
    node.setAttribute('name', 'chbPropertyNames');
    node.setAttribute('unchecked', 'unchecked');
    node.setAttribute('onchange', 'reverseChecked(this)');

    var h4 = document.createElement('h4');
    h4.setAttribute('float', 'left');
    h4.setAttribute('name', 'propertyName');

    var text = document.createTextNode(value);
    h4.appendChild(text);
    label.appendChild(node);
    label.appendChild(h4);
    div2.appendChild(label);
    div1.appendChild(div2);
    parent.appendChild(div1);
}

function addACheckBoxWithTwoTexts(parent, value1, value2) {


    var tr = document.createElement('tr');
    var td1 = document.createElement('td');
    var checkBox = document.createElement('input');
    checkBox.setAttribute('type', 'checkbox');
    checkBox.setAttribute('name', 'chbStrings');
    checkBox.setAttribute('unchecked', 'unchecked');
    checkBox.setAttribute('onchange', 'reverseChecked(this)');
    td1.appendChild(checkBox);

    var td2 = document.createElement('td');
    td2.setAttribute('name', 'string');
    var text1 = document.createTextNode(value1);
    td2.appendChild(text1);

    var td3 = document.createElement('td');
    td3.setAttribute('name', 'stringDetail');
    var text2 = document.createTextNode(value2);
    td3.appendChild(text2);

    tr.appendChild(td1);
    tr.appendChild(td2);
    tr.appendChild(td3);
    parent.appendChild(tr);
}

/**
 * 提交表单
 */
function submitForm() {
    var bigArray = document.getElementsByName('chbBigArray')[0].value === "checked" ? 1 : 0;
    var calculate = document.getElementsByName('chbComputation')[0].value === "checked" ? 1 : 0;
    var strength = parseInt(document.getElementsByName('txtComputationParam')[0].value);
    var controlFlowFlatten = document.getElementsByName('chbControlFlowFlatten')[0].value === "checked" ? 1 : 0;
    var thresholdValue = parseInt(document.getElementsByName('txtThresholdValue')[0].value);
    var blockSize = parseInt(document.getElementsByName('txtBlockSize')[0].value);
    var numberHandling = document.getElementsByName('chbStringAndNumber')[0].value === "checked" ? 1 : 0;
    var reserveNames = document.getElementsByName('txtReserveName')[0].value.trim().split(" ");
    var deadCode = document.getElementsByName('chbDeadCode')[0].value === "checked" ? 1 : 0;

    var propertyNames = "";
    var elements1 = document.getElementsByName('chbPropertyNames');
    for (var i = 0; i < elements1.length; i++)
        propertyNames += elements1[i].getAttribute('unchecked') === "checked" ? "1" : "0";

    var strings = "";
    var elements2 = document.getElementsByName('chbStrings');
    for (var j = 0; j < elements2.length; j++)
        strings += elements2[j].getAttribute('unchecked') === "checked" ? "1" : "0";

    var xmlhttp;
    if (window.XMLHttpRequest)
        xmlhttp = new XMLHttpRequest();
    else
        xml = new ActiveXObject("Microsoft.XMLHTTP");

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
            window.location.href = "index.jsp";
    };

    xmlhttp.open("POST", "Obfuscation.jsp", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("bigArray=" + bigArray + "&calculate=" + calculate + "&strength=" + strength +
        "&controlFlowFlatten=" + controlFlowFlatten + "&thresholdValue=" + thresholdValue + "&blockSize="
        + blockSize + "&numberHandling=" + numberHandling + "&reserveNames="
        + reserveNames + "&deadCode=" + deadCode + "&propertyNames=" + propertyNames + "&strings=" + strings);
}

function analyzeFile() {
    //属性名
    var textOfPropertyNames = document.getElementsByTagName('propertyNames')[0].innerHTML;
    var propertyNameArray = textOfPropertyNames.trim().split(" ");
    for (var i = 0; i < propertyNameArray.length; i++)
        addACheckBoxWithOneText(document.getElementById('content_left'), propertyNameArray[i]);

    //字符串及其详情
    var textOfStrings = document.getElementsByTagName('strings')[0].innerHTML;
    var textOfStringDetails = document.getElementsByTagName('stringDetails')[0].innerHTML;
    var stringArray = textOfStrings.trim().split(" ");
    var stringDetailArray = textOfStringDetails.trim().split("!@#");
    for (var j = 0; j < stringArray.length; j++)
        addACheckBoxWithTwoTexts(document.getElementById('content_right'), stringArray[j], stringDetailArray[j]);
}

function chooseall1() {
    var elements = document.getElementsByName('chbPropertyNames');
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute('unchecked') === 'unchecked') {
            elements[i].setAttribute('unchecked', 'checked');
            elements[i].checked = true;
        }
    }
}

function obchoose1() {
    var results = document.getElementsByName('chbPropertyNames');
    for (var i = 0; i < results.length; i++) {
        if (results[i].getAttribute('unchecked') === 'unchecked') {
            results[i].setAttribute('unchecked', 'checked');
        } else {
            results[i].setAttribute('unchecked', 'unchecked');
        }
        results[i].checked = !(results[i].getAttribute('unchecked') === 'unchecked');
    }
}

function not1() {
    var nts = document.getElementsByName('chbPropertyNames');
    for (var i = 0; i < nts.length; i++) {
        if (nts[i].getAttribute('unchecked') === 'checked') {
            nts[i].setAttribute('unchecked', 'unchecked');
            nts[i].checked = false;
        }
    }
}

function chooseall2() {
    var elements = document.getElementsByName('chbStrings');
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute('unchecked') === 'unchecked') {
            elements[i].setAttribute('unchecked', 'checked');
            elements[i].checked = true;
        }
    }
}

function obchoose2() {
    var results = document.getElementsByName('chbStrings');
    for (var i = 0; i < results.length; i++) {
        if (results[i].getAttribute('unchecked') === 'unchecked') {
            results[i].setAttribute('unchecked', 'checked');
        } else {
            results[i].setAttribute('unchecked', 'unchecked');
        }
        results[i].checked = !(results[i].getAttribute('unchecked') === 'unchecked');
    }
}

function not2() {
    var nts = document.getElementsByName('chbStrings');
    for (var i = 0; i < nts.length; i++) {
        if (nts[i].getAttribute('unchecked') === 'checked') {
            nts[i].setAttribute('unchecked', 'unchecked');
            nts[i].checked = false;
        }
    }
}
