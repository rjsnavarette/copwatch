$(document).ready(function() {
  $(".alert.alert-success, .alert.alert-danger")
  .fadeOut(5000, function() {
    $(".alert").addClass("hide").removeClass("alert-success")
      .removeClass("alert-danger").css("display", "block").text("No Message");
  });
})