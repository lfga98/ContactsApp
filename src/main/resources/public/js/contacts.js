$( document ).ready(function() {
    $('#notifications').hide()
    $('#notifications').removeClass("alert alert-warning alert")
    $('#notifications').text("");




    $.get( "/contacts", function( data ) {
        let contactInfo ="";
        contactInfo="<table class='table table-striped paginated' id='contactsTable'><thead class='table-header'>" +
            "<tr>" +
            "<td>ID</td>" +
            "<td>Name</td>" +
            "<td>Last Name</td>" +
            "<td>Company</td>"+
            "<td>Phone number</td>" +
            "<td>Email</td>" +
            "<td></td>" +
            "<td><a type=\"button\" id='new_contact' class=\"btn btn-success text-light\"><i class=\"fa fa-plus\"></i></a></td>" +
            "</tr>" +
            "</thead><tbody>";
        $.each(data,(i,item) => {
            contactInfo+="<tr>";
            contactInfo+="<td>"+item.id+"</td>";
            contactInfo+="<td>"+item.name+"</td>";
            contactInfo+="<td>"+item.lastName+"</td>";
            contactInfo+="<td>"+item.company+"</td>";
            contactInfo+="<td>"+item.phoneNumber+"</td>";
            contactInfo+="<td>"+item.email+"</td>";
            contactInfo+="<td><a type=\"button\" class=\"btn btn-warning\"><i class=\"fa fa-pencil\"></i></a></td>";
            contactInfo+="<td><a type='button' class='btn btn-danger'  ><i class=\"fa fa-times\"></i></a></td>";
            contactInfo+="</tr>"

        });
        contactInfo+="</tbody></table>";
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

        function deleteFunctionality(context) {
            if (!confirm("Are you sure you want to delete this contact?")){
                return false;
            }
            row = $(context).closest("tr");
            id = row.find("td:nth-child(1)");
            fetch("/contacts/"+id.text(), {
                method: 'DELETE'
            }).then(response => response.json())
                .then(function(data) {
                    if (data.hasOwnProperty("statusCode")){
                        if (data.statusCode==204){
                            $("#notifications").show()
                            $("#notifications").className="alert alert-warning";
                            $("#notifications").text("");
                            $("#notifications").text(data.message);
                        }
                    }
                    if (data.hasOwnProperty("id")){
                        $("#notifications").show()
                        $("#notifications").addClass("alert");
                        $("#notifications").addClass("alert-success");
                        $("#notifications").text("");
                        $("#notifications").text("Contact Deleted!");
                        row.remove();
                    }
                }).catch(function(error) {
                $("#notifications").show()
                $("#notifications").addClass("alert");
                $("#notifications").addClass("alert-danger");
                $("#notifications").text("");
                $("#notifications").text("Internal error " + error);
            });
        }



        $(".btn-danger").click(function () {
            deleteFunctionality(this)
        });

        $("#new_contact").click(function () {
            $("#titleModal").text("New Contact");
            $("#miModal").modal("show");
            $("#miModal #btnUpdateModal").hide()
            $("#miModal #btnSaveModal").show()
        })

        $("#miModal #btnSaveModal").click(function (){
            id=$("#miModal #contactId").val();
            if (id==-1){
                let contact={
                    name:$("#miModal #nameInput").val(),
                    lastName:$("#miModal #lastNameInput").val(),
                    email:$("#miModal #emailInput").val(),
                    phoneNumber:$("#miModal #phoneNumberInput").val(),
                    company:$("#miModal #companyInput").val()
                }
                let contactJson=JSON.stringify(contact);
                console.log(contactJson)
                $.ajax({
                    url: '/contacts/',
                    dataType: 'json',
                    type: 'post',
                    contentType: 'application/json',
                    data: contactJson,
                    success: function( data, textStatus, jQxhr ){
                        if (data.hasOwnProperty("statusCode")){
                            if (data.statusCode==400){
                                $("#miModal #notificationsModal").addClass("alert");
                                $("#miModal #notificationsModal").addClass("alert-danger");
                                $("#miModal #notificationsModal").text("");
                                $("#miModal #notificationsModal").text(data.message);
                            }
                        }
                        else if (data.hasOwnProperty("id")){
                            $("#miModal").modal("hide");
                            console.log(data.id);
                            $('#contactsTable').append('<tr><td>'+data.id+'</td><td>'+data.name+'</td><td>'+data.lastName+'</td>' +
                                '<td>'+data.company+'</td><td>'+data.phoneNumber+'</td><td>'+data.email+'</td>' +
                                '<td><a type=\"button\" class=\"btn btn-warning\"><i class=\"fa fa-pencil\"></i></a></td>' +
                                '<td><a type=\'button\' class=\'btn btn-danger\'  ><i class=\"fa fa-times\"></i></a></td>' +
                                '</tr>');
                            $("#notifications").show()
                            $("#notifications").addClass("alert");
                            $("#notifications").addClass("alert-success");
                            $("#notifications").text("");
                            $("#notifications").text("Contact Created!");
                            $(".btn-danger").click(function () {
                                deleteFunctionality($('#listaContactos tr:last'));
                            });
                        }
                    },
                    error: function( jqXhr, textStatus, errorThrown ){
                        $("#miModal").modal("hide");
                        $("#notifications").addClass("alert");
                        $("#notifications").addClass("alert-danger");
                        $("#notifications").text("");
                        $("#notifications").text("Internal error");
                    }
                });
            }
        });










    });

});