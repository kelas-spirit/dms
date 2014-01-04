function go(url)
{
    window.location = url;
}

function deleteDocument(url)
{
    var isOK = confirm("Are you sure you want to delete Document ?\nThis action can not be reversed.");
    if(isOK)
    {
        go(url);
    }
}

