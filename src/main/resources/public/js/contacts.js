function showContacts() {
    $.get("/contacts", function (data) {
        let contactInfo = "";
        contactInfo = "<table class='table table-striped paginated table-responsive' id='contactsTable'><thead class='table-header'>" +
            "<tr>" +
            "<td>ID</td>" +
            "<td>Name</td>" +
            "<td>Last Name</td>" +
            "<td>Company</td>" +
            "<td>Phone number</td>" +
            "<td>Email</td>" +
            "<td></td>" +
            "<td><a type=\"button\" id='new_contact' class=\"btn btn-success text-light\"><i class=\"fa fa-plus\"></i></a></td>" +
            "</tr>" +
            "</thead><tbody>";
        $.each(data, (i, item) => {
            contactInfo += "<tr>";
            contactInfo += "<td>" + item.id + "</td>";
            contactInfo += "<td>" + item.name + "</td>";
            contactInfo += "<td>" + item.lastName + "</td>";
            contactInfo += "<td>" + item.company + "</td>";
            contactInfo += "<td>" + item.phoneNumber + "</td>";
            contactInfo += "<td>" + item.email + "</td>";
            contactInfo += "<td><a type=\"button\" class=\"btn btn-warning\"><i class=\"fa fa-pencil\"></i></a></td>";
            contactInfo += "<td><a type='button' class='btn btn-danger'  ><i class=\"fa fa-times\"></i></a></td>";
            contactInfo += "</tr>"

        });
        contactInfo += "</tbody></table>";
        $("#listaContactos").html(contactInfo);

        $('table.paginated').each(function () {
            var currentPage = 0;
            var numPerPage = 10; // number of items
            var $table = $(this);

            $table.bind('repaginate', function () {
                $table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
            });
            $table.trigger('repaginate');
            var numRows = $table.find('tbody tr').length;
            var numPages = Math.ceil(numRows / numPerPage);
            var $pager = $('<div class="pager"></div>');
            for (var page = 0; page < numPages; page++) {
                $('<span class="btn btn-light"></span>').text(page + 1).bind('click', {
                    newPage: page
                }, function (event) {
                    currentPage = event.data['newPage'];
                    $table.trigger('repaginate');
                    $(this).addClass('active').siblings().removeClass('active');
                }).appendTo($pager).addClass('clickable');
            }
            if (numRows > numPerPage) {
                $pager.insertAfter($table).find('span.page-number:first').addClass('active');
            }
        });

        $(".btn-danger").click(function () {
            deleteFunctionality(this)
        });

        $("#new_contact").click(function () {
            $("#titleModal").text("New Contact");
            $("#miModal").modal("show");
            $("#miModal #btnUpdateModal").hide()
            $("#miModal #btnSaveModal").show()
            cleanForm();
        });

        $(".btn-warning").click(function () {
            $("#titleModal").text("Edit Contact");
            $("#miModal").modal("show");
            $("#miModal #btnSaveModal").hide()
            $("#miModal #btnUpdateModal").show()
            cleanForm();
            row = $(this).closest("tr");
            id = row.find("td:nth-child(1)");
            $.get("/contacts/"+id.text(), function (data) {
                $("#miModal #contactId").val(data.id);
                $("#miModal #nameInput").val(data.name);
                $("#miModal #lastNameInput").val(data.lastName);
                $("#miModal #emailInput").val(data.email);
                $("#miModal #phoneNumberInput").val(data.phoneNumber);
                $("#miModal #companyInput").val(data.company);

            }).fail(function() {
                $("#miModal").modal("hide");
                let notifications=$("#notifications");
                notifications.show()
                notifications.addClass("alert");
                notifications.addClass("alert-danger");
                notifications.text("");
                notifications.text("Internal error");
            });

        });


    });
}


function deleteFunctionality(context) {
    if (!confirm("Are you sure you want to delete this contact?")) {
        return false;
    }
    row = $(context).closest("tr");
    id = row.find("td:nth-child(1)");
    fetch("/contacts/" + id.text(), {
        method: 'DELETE'
    }).then(response => response.json())
        .then(function (data) {
            $("#notifications").show()
            $("#notifications").addClass("alert");
            $("#notifications").addClass("alert-success");
            $("#notifications").text("");
            $("#notifications").text("Contact Deleted!");
            showContacts();
        }).catch(function (error) {
        $("#notifications").show()
        $("#notifications").addClass("alert");
        $("#notifications").addClass("alert-danger");
        $("#notifications").text("");
        $("#notifications").text("Internal error " + error);
    });
}


function validateFields(){
    let name= $("#miModal #nameInput").val();
    let lastName= $("#miModal #lastNameInput").val();
    let email= $("#miModal #emailInput").val();
    let phoneNumber= $("#miModal #phoneNumberInput").val();
    let errors=false;
    let message="";

    if (name===""){
        errors=true;
        message+="-Name can't be empty </br>";
    }
    if (name.match(".*\\d.*")){
        errors=true;
        message+="-Name can't contain digits </br>";
    }

    if (lastName===""){
        errors=true;
        message+="-Last name can't be empty </br>";
    }
    if (lastName.match(".*\\d.*")){
        errors=true;
        message+="-Last name can't contain digits </br>";
    }

    if (phoneNumber.length!=0&&!allnumeric(phoneNumber)){
        errors=true;
        message+="-Phone number must contain only digits </br>";
    }
    if (email===""){
        errors=true;
        message+="-Email can't be empty </br>";
    }
    if (!validateEmail(email)){
        errors=true;
        message+="-Email must be a valid email format </br>";
    }

    if (!errors){
        return true;
    }else{
        let notifications=$("#miModal #notificationsModal");
        notifications.addClass("alert");
        notifications.addClass("alert-danger");
        notifications.text("");
        notifications.html(message);
    }
}

function restartModalNotfications(){
    let notifications=$("#miModal #notificationsModal").val("");
    notifications.removeClass("alert");
    notifications.removeClass("alert-danger");
    notifications.removeClass("alert-success");
    notifications.text("");

}

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

function allnumeric(inputtxt) {
    var numbers = /^[0-9]+$/;
    if(inputtxt.match(numbers)) {
        return true;
    }
    else {
        return false;
    }
}

function cleanForm(){
    $("#miModal #contactId").val(-1);
    $("#miModal #nameInput").val("");
    $("#miModal #lastNameInput").val("");
    $("#miModal #emailInput").val("");
    $("#miModal #phoneNumberInput").val("");
    $("#miModal #companyInput").val("");
    restartModalNotfications();
}





$(document).ready(function () {
    $('#notifications').hide()
    $('#notifications').removeClass("alert alert-warning alert")
    $('#notifications').text("");
    showContacts();

    $("#miModal #btnSaveModal").click(function () {
        id = $("#miModal #contactId").val();
        if (id == -1 && validateFields()) {
            let contact = {
                name: $("#miModal #nameInput").val(),
                lastName: $("#miModal #lastNameInput").val(),
                email: $("#miModal #emailInput").val(),
                phoneNumber: $("#miModal #phoneNumberInput").val(),
                company: $("#miModal #companyInput").val()
            }
            let contactJson = JSON.stringify(contact);
            $.ajax({
                url: '/contacts/',
                dataType: 'json',
                type: 'post',
                contentType: 'application/json',
                data: contactJson,
                success: function (data, textStatus, jQxhr) {
                    $("#miModal").modal("hide");
                    let notificationsBar = $("#notifications")
                    notificationsBar.show()
                    notificationsBar.addClass("alert");
                    notificationsBar.addClass("alert-success");
                    notificationsBar.text("");
                    notificationsBar.text("Contact Created!");
                    showContacts();
                    cleanForm();
                },
                error: function (jqXhr, textStatus, errorThrown) {
                    $("#miModal #notificationsModal").addClass("alert");
                    $("#miModal #notificationsModal").addClass("alert-danger");
                    $("#miModal #notificationsModal").text("");
                    $("#miModal #notificationsModal").text(jqXhr.responseJSON.message);
                }
            });
        }
    });


    $("#miModal #btnUpdateModal").click(function () {
        id = $("#miModal #contactId").val();
        if (id != -1 && validateFields()) {
            let contact = {
                id:$("#miModal #contactId").val(),
                name: $("#miModal #nameInput").val(),
                lastName: $("#miModal #lastNameInput").val(),
                email: $("#miModal #emailInput").val(),
                phoneNumber: $("#miModal #phoneNumberInput").val(),
                company: $("#miModal #companyInput").val()
            }
            let contactJson = JSON.stringify(contact);
            $.ajax({
                url: '/contacts/'+id,
                dataType: 'json',
                type: 'put',
                contentType: 'application/json',
                data: contactJson,
                success: function (data, textStatus, jQxhr) {
                    $("#miModal").modal("hide");
                    let notificationsBar = $("#notifications")
                    notificationsBar.show()
                    notificationsBar.addClass("alert");
                    notificationsBar.addClass("alert-success");
                    notificationsBar.text("");
                    notificationsBar.text("Contact Edited!");
                    showContacts();
                    cleanForm();
                },
                error: function (jqXhr, textStatus, errorThrown) {
                    $("#miModal #notificationsModal").addClass("alert");
                    $("#miModal #notificationsModal").addClass("alert-danger");
                    $("#miModal #notificationsModal").text("");
                    $("#miModal #notificationsModal").text(jqXhr.responseJSON.message);
                }
            });
        }
    });











});