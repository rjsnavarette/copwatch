$(document).ready(function() {
  $(".alert.alert-success, .alert.alert-danger")
  .fadeOut(5000, function() {
    $(".alert").addClass("hide").removeClass("alert-success")
      .removeClass("alert-danger").css("display", "block").text("No Message");
  })

  $("#DataTables_Users, #DataTables_Feedbacks, #DataTables_EmailTemplates").on("click", ".action-delete", function(e) {
    console.log("action delete clicked");
    e.preventDefault();

    create_delete_alert($(this).attr("data-target"));
  })

  $("form").submit(function() {
    show_loading();
  })

  fadeAlert();

  // functions
  function fadeAlert() {
    $(".alert.alert-success, .alert.alert-danger")
    .fadeOut(5000, function() {
      $(".alert-success, .alert-danger").addClass("hidden").addClass("no-messsage").removeClass("alert-success")
        .removeClass("alert-danger").css("display", "block").text("No Message");
    });
  }

  function create_delete_alert(target) {
    swal({
        title: "Are you sure?",
        text: "You will not be able to recover the data!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
      console.log("confirm delete clicked");
      console.log(target);
      $(target)[0].click();
    });
  }

  function show_loading() {
    $(".alert-loading").removeClass('hidden');
  }
})