$(document).ready(function() {
  fadeAlert();

  $("form .edit").on("click", function(e) {
    e.preventDefault();
    let txt = $(this).text();

    if (txt == "Edit") {
      $("#page_title, #page_content").attr("readonly", false);
      $("form .save").removeClass("hide");
      $(this).text("Cancel");
    } else {
      $("#page_title, #page_content").attr("readonly", true);
      $("form .save").addClass("hide");
      $(this).text("Edit")
    }
  })

  function fadeAlert() {
    $(".alert.alert-info, .alert.alert-danger")
    .fadeOut(5000, function() {
      $(".alert").addClass("hide").removeClass("alert-info")
        .removeClass("alert-danger").css("display", "block").text("No Message");
    });
  }
})