$(function() {

    $('#login-button').button().click(function() {
        $('#login-div').show();
        $('#signup-div').hide();
    });

    $('#signup-button').button().click(function() {
        $('#signup-div').show();
        $('#login-div').hide();
    });

    $('#signup-form').submit(function(e) {
        e.preventDefault();
        postForm('service/createpbx', '#signup-form',
                function(data, textStatus, jqXHR) {
                    alert('worked!');
                },
                function() {
                    alert('failed');
                });
    });

    $('#login-form').submit(function(e) {
        e.preventDefault();
        postForm('service/login', '#login-form',
                function(data, textStatus, jqXHR) {
                    alert('worked!');
                },
                function() {
                    alert('failed');
                });
    });
});


function postForm(path, formId, successFn, errorFn) {
    $.ajax({
        url: path,
        type: 'POST',
        data: $(formId).serialize(),
//        contentType: 'application/x-www-form-urlencoded',
        success: successFn,
        error: errorFn
    });
}

function post(path, successFunction, jsonPayload, contentType, errorFunction) {
    request(path, mediaType, successFunction, 'POST', jsonPayload, contentType, errorFunction);
}


function request(path, successFunction, httpMethod, jsonPayload, contentType, errorFunction) {

    $.ajax({
        url: '/' + path,
        type: httpMethod,
        data: jsonPayload,
        dataType: 'json',
        success: successFunction,
        error: errorFunction
//        beforeSend: function setHeader(xhr) {
//            xhr.setRequestHeader('Accept', mediaType);
//
//            if (typeof contentType !== "undefined") {
//                xhr.setRequestHeader('Content-Type', contentType);
//            }
//            xhr.setRequestHeader('Authorization', 'Basic ' + hashedAuthenticationToken);
//        }
    });
}
