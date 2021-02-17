$(document).ready(function() {
  new FroalaEditor('#page_content', {})

  $("form .edit").on("click", function(e) {
    e.preventDefault();
    let txt = $(this).text();

    if (txt == "Edit") {
      $("#page_title, #page_content").attr("readonly", false);
      $("form .save").removeClass("hide");
      $(this).text("Cancel");
      $("form textarea.no-resize").addClass("hide");
      $("form .fr-box").css("display", "block");
    } else {
      $("#page_title, #page_content").attr("readonly", true);
      $("form .save").addClass("hide");
      $(this).text("Edit")
      $("form textarea.no-resize").removeClass("hide");
      $("form .fr-box").css("display", "none");
    }
  })
})