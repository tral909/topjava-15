const ajaxUrl = "ajax/admin/users/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});

function updateTable() {
    $.get(ajaxUrl, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function changeEnabled(id_user, enabled) {
    //$('#datatable input[name="user_enabled"]').change(function () {
        // this will contain a reference to the checkbox
        $.ajax({
            type: "POST",
            url: ajaxUrl + "enable",
            data: "id=" + id_user + "&enabled=" + enabled,
        }).done(function () {
            updateTable();
        });
        /*if (this.checked) {
            // the checkbox is now checked

        } else {
            // the checkbox is now no longer checked

        }*/
    //});
}
