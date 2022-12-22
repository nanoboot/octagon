function setCookie(cname, cvalue) {
    setCookie(cname, cvalue, 90);
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function showColumns(classNames) {
    showHideColumns(classNames, 'table-cell');
}

function isFunctionDefined(functionName) {
    if (eval("typeof(" + functionName + ") == typeof(Function)")) {
        return true;
    }
}

function hideColumns() {
    if (!isFunctionDefined('getEntityId')) {
        console.log("Function getEntityId is not defined, exiting.");
        return;
    }
    entityId = getEntityId();

    cookieName = entityId.concat('_hideColumns');
    cookieValue = getCookie(cookieName);
    //alert(cookieValue);
    classNames = cookieValue.split(",");

    for (var i = 0; i < classNames.length; i++) {
        className = classNames[i];
        if (className.length == 0) {
            continue;
        }
        classNames[i] = ("column_" + className);
    }

    showHideColumns(classNames, 'none');
}

function showHideColumns(classNames, display) {
    for (var i = 0; i < classNames.length; i++) {
        var className = classNames[i];
        if (className.toString().length == 0) {
            continue;
        }
        showHideColumn(className, display);
    }
}

/**
 * Flips column visibility.
 */
function flipColumnVisibility(className) {
    var cells = document.getElementsByClassName(className);
    var display = 'table-cell';
    for (var i = 0; i < cells.length; i++) {
        var cell = cells.item(i);
        if (cell.style.display === 'none') {
            display = 'table-cell';
        } else
            display = 'none';
        break;
    }

    showHideColumn(className, display);
}

function showColumn(className) {
    showHideColumn(className, 'table-cell');
}

function hideColumn(className) {
    showHideColumn(className, 'none');
}

function showHideColumn(className, display) {
    if (className.toString().length == 7)
        throw "classnName is missing the entityId: " + className;
    var cells = document.getElementsByClassName(className);
    for (var i = 0; i < cells.length; i++) {
        var cell = cells.item(i);
        cell.style.display = display;
        //alert(cell.style.display);
    }
    entityId = getEntityId();
    cookieName = entityId.concat('_hideColumns');
    currentCookie = getCookie(cookieName);

    toSearch = className.replace('column_', '').concat(',');

    var flipLabel = document.getElementById('flipLabel_' + className.replace("column_", ""));
    //alert('flipLabel_' + className);
    if (display === 'table-cell') {
        //going to hide
        currentCookie = currentCookie.replace(toSearch, '');
        setCookie(cookieName, currentCookie);
        //flipLabel.innerText = 'Hide';
        flipLabel.style.backgroundColor = '#1E90FF'
        flipLabel.style.fontSize = '110%';
        flipLabel.style.padding = '10px';
        flipLabel.style.margin = '5px';
    } else {
        //going to show
        if (!currentCookie.includes(toSearch)) {
            setCookie(cookieName, currentCookie.concat(toSearch));
        }
        //flipLabel.innerText = 'Show';
        flipLabel.style.backgroundColor = '#DCDCDC'
        flipLabel.style.fontSize = '85%';
        flipLabel.style.padding = '5px';
        flipLabel.style.paddingLeft = '0px';
        flipLabel.style.paddingRight = '0px';
        flipLabel.style.margin = '2px';
    }

}

function onPageLoad() {
    hideColumns();
}

function hideColumnWithoutCookies(className) {
    var cells = document.getElementsByClassName(className);
    for (var i = 0; i < cells.length; i++) {
        var cell = cells.item(i);
        cell.style.display = 'none';
    }
}

function showColumnsWithoutCookies(classNames) {
    for (var i = 0; i < classNames.length; i++) {
        var className = classNames[i];
        if (className.toString().length == 0) {
            continue;
        }
        showColumnWithoutCookies(className);
    }
}
function showColumnWithoutCookies(className) {
    var cells = document.getElementsByClassName(className);
    for (var i = 0; i < cells.length; i++) {
        var cell = cells.item(i);
        cell.style.display = 'table-cell';
    }
}
