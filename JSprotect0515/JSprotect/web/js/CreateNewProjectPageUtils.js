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

function changeMaxValue() {
    document.getElementsByName('txtBlockSize')[0].max = document.getElementsByName('txtThresholdValue')[0].value - 1;
}
