(function($) {
  "use strict"; // Start of use strict

  fetch('https://api.bee.energy:8000/current')
    .then(function(response) {
      response.text().then(function (text) {
          const json = JSON.parse(text);
          let price = Math.round(json.spotPrice/10);
          const d = new Date();
          const imgNo = Math.round(100*Math.random()) % 2;

        //James change here:
          let percent = 8;
           $("#energy-msg1").text(+ percent + "% renewables right now");
          if(percent < 10) {
                $("#energy-msg2").text("Postpone power usage if possible");
                $("#background").removeClass("bg-primary");
                $("#background").addClass("bg-danger");
                $("#background1").removeClass("bg-primary");
                $("#background1").addClass("bg-danger");
                $("#avatar").attr("src","img/scared-" + imgNo + ".svg");
          } else {
                let r = [
                    "Roast that chicken!",
                    "Grill the veggies!",
                    "Do your laundry!",
                    "Iron your shirts!",
                    "Dry your clothes!",
                    "Make a cuppa!",
                    "Vacuum the carpet!"];
                let rMsg = r[Math.round(100*Math.random()) % r.length];
                $("#energy-msg2").text(rMsg);
                $("#background").addClass("bg-primary");
                $("#background").removeClass("bg-danger");
                $("#background1").addClass("bg-primary");
                $("#background1").removeClass("bg-danger");
                $("#avatar").attr("src","img/happy-" + imgNo + ".svg");
          }
          $("#price").text("Current energy price is " + price + " cents per kWh.");
      });
    });

  // Smooth scrolling using jQuery easing
  $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function() {
    if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
      if (target.length) {
        $('html, body').animate({
          scrollTop: (target.offset().top - 71)
        }, 1000, "easeInOutExpo");
        return false;
      }
    }
  });

  // Scroll to top button appear
  $(document).scroll(function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Closes responsive menu when a scroll trigger link is clicked
  $('.js-scroll-trigger').click(function() {
    $('.navbar-collapse').collapse('hide');
  });

  // Activate scrollspy to add active class to navbar items on scroll
  $('body').scrollspy({
    target: '#mainNav',
    offset: 80
  });

  // Collapse Navbar
  var navbarCollapse = function() {
    if ($("#mainNav").offset().top > 100) {
      $("#mainNav").addClass("navbar-shrink");
    } else {
      $("#mainNav").removeClass("navbar-shrink");
    }
  };
  // Collapse now if page is not at top
  navbarCollapse();
  // Collapse the navbar when page is scrolled
  $(window).scroll(navbarCollapse);

  // Floating label headings for the contact form
  $(function() {
    $("body").on("input propertychange", ".floating-label-form-group", function(e) {
      $(this).toggleClass("floating-label-form-group-with-value", !!$(e.target).val());
    }).on("focus", ".floating-label-form-group", function() {
      $(this).addClass("floating-label-form-group-with-focus");
    }).on("blur", ".floating-label-form-group", function() {
      $(this).removeClass("floating-label-form-group-with-focus");
    });
  });

})(jQuery); // End of use strict
