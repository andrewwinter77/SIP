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

function post(path, jsonPayload, contentType, successFn, errorFn) {
    request(path, 'POST', jsonPayload, contentType, successFn, errorFn);
}

function get(path, contentType, successFn, errorFn) {
    request(path, 'GET', {}, contentType, successFn, errorFn);
}

function call(sipAddress) {
    post('service/c2c/' + sipAddress, {}, null,
            function(data, textStatus, jqXHR) {
                alert('calling');
            },
            function(jqXHR, textStatus, errorThrown) {
                alert('error calling');
            });
}

$(function() {

    function resetNotLoggedInDiv() {
        $('#login-div').hide();
        $('#signup-div').hide();
        $('#login-div-error').hide();
        $('#login-form-email').text('');
        $('#login-form-password').text('');
    }

    $('#create-user-button').button().click(function() {
        $('#create-user-div').show();
    });

    $('#login-button').button().click(function() {
        $('#signup-div').hide();
        $('#login-div').show();
        $('#login-form-email').focus();
    });

    $('#register-button').button().click(function() {
        $('#signup-div').show();
        $('#login-div').hide();
    });

    $('#phone-extensions-button').click(function() {
        get('service/user', null,
                function(data, textStatus, jqXHR) {
                    var items = [];
                    $.each(data, function(i, val) {
                        items.push('<li><a href="#" onclick="call(\'sip:' + val.userPart + '@sip.sipseer.com\'); return false;">' + val.forename + ' ' + val.surname + '</li>');
                    });
                    $('#phone-extensions-list').empty();
                    $('#phone-extensions-list').append(items.join(''));
                    $('#phone-extensions-div').show();
                },
                function(jqXHR, textStatus, errorThrown) {
                    $('#phone-extensions-list').empty();
                    $('#phone-extensions-div').show();
                });

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

    $('#create-user-form').submit(function(e) {
        e.preventDefault();
        postForm('service/user/create', '#create-user-form',
                function(data, textStatus, jqXHR) {
                    alert('worked!');
                },
                function(jqXHR, textStatus, errorThrown) {
                    alert('failed');
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

