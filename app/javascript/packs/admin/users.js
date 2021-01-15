$(document).ready(function() {
  $("#uploadPhoto").on("click", function() {
    $("#user_photo").trigger("click");
  })

  $("#user_photo").on("change", function() {
    readURL(this);
  });

  function readURL(input) {
    if (input.files && input.files[0]) {
      var reader = new FileReader();

      reader.onload = function(e) {
        $(".member-card .member-img img").attr("src", e.target.result);
      }

      reader.readAsDataURL(input.files[0]);
    }
  }
})