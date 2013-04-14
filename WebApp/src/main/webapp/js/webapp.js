$(function() {

    function resetNotLoggedInDiv() {
        $('#login-div').hide();
        $('#signup-div').hide();
        $('#login-div-error').hide();
        $('#login-form-email').text('');
        $('#login-form-password').text('');
    }

    $('#login-button').button().click(function() {
        $('#signup-div').hide();
        $('#login-div').show();
        $('#login-form-email').focus();
    });

    $('#register-button').button().click(function() {
        $('#signup-div').show();
        $('#login-div').hide();
    });

    $('#admin-button').button().click(function() {
        $('#admin-div').show();
    });

    $('#logout-button').button().click(function() {
        post('service/logout', {}, null,
                function(data, textStatus, jqXHR) {
                    resetNotLoggedInDiv();
                    $('#logged-in-div').hide();
                    $('#not-logged-in-div').show();
                },
                function(jqXHR, textStatus, errorThrown) {
                    resetNotLoggedInDiv();
                    $('#logged-in-div').hide();
                    $('#not-logged-in-div').show();
                });
    });

    $('#signup-form').submit(function(e) {
        e.preventDefault();
        postForm('service/createpbx', '#signup-form',
                function(data, textStatus, jqXHR) {
                    alert('worked!');
                },
                function(jqXHR, textStatus, errorThrown) {
                    alert('failed');
                });
    });

    $('#login-form').submit(function(e) {
        e.preventDefault();
        postForm('service/login', '#login-form',
                function(data, textStatus, jqXHR) {
                    $('#not-logged-in-div').hide();
                    $('#user-name-span').text(data.forename + ' ' + data.surname);
                    
                    if (data.adminUser === true) {
                        $('#admin-button-span').show();
                    } else {
                        $('#admin-button-span').hide();
                    }
                    $('#admin-div').hide();
                    $('#logged-in-div').show();
                },
                function(jqXHR, textStatus, errorThrown) {
                    if (jqXHR.status === 403) {
                        $('#login-div-error').show();
                    } else {
                        alert('failed');
                    }
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

function post(path, jsonPayload, contentType, successFn, errorFn) {
    request(path, 'POST', jsonPayload, contentType, successFn, errorFn);
}


function request(path, httpMethod, jsonPayload, contentType, successFn, errorFn) {

    $.ajax({
        url: path,
        type: httpMethod,
        data: jsonPayload,
        dataType: 'json',
        success: successFn,
        error: errorFn
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
