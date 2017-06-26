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

function setDisabledOfStringAndNumber() {
    if (document.getElementsByName("chbStringAndNumber")[0].value === "unchecked") {
        document.getElementsByName("chbString")[0].removeAttribute("disabled");
        document.getElementsByName("chbNumber")[0].removeAttribute("disabled");
        document.getElementsByName("chbStringAndNumber")[0].value = "checked";
    } else {
        document.getElementsByName("chbString")[0].setAttribute("disabled", "disabled");
        document.getElementsByName("chbNumber")[0].setAttribute("disabled", "disabled");
        document.getElementsByName("chbStringAndNumber")[0].value = "unchecked";
    }
}

function reverseValue(element) {
    if (element.value ==="unchecked")
        element.value = "checked";
    else
        element.value = "unchecked";
}
