const ajaxUrl = "ajax/profile/meals/";
let ajaxUrlWithFilter;
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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

function between() {
    let form = $('#betweenForm');
    ajaxUrlWithFilter = ajaxUrl + 'between?' + form.serialize();
    $.get(ajaxUrlWithFilter, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function dropFilter() {
    $('#betweenForm').find(':input').val('');
    ajaxUrlWithFilter = ajaxUrl;
    $.ajax({
        url: ajaxUrl,
        type: 'GET',
    }).done(function () {
       updateTable();
    });
}

function updateTable() {
    $.get(ajaxUrlWithFilter, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}