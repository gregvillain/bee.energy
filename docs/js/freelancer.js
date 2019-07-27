(function($) {
  "use strict"; // Start of use strict

  function update() {
       fetch("https://data.opennem.org.au/power/nsw1.json")
      .then(function(response) {
        return response.json();
      })
      .then(function(x) {
          const black_coal = x[0].history.data;
          const distillate = x[1].history.data;
          const gas_ccgt = x[2].history.data;
          const gas_ocgt = x[3].history.data;
          const hydro = x[4].history.data;
          const pumps = x[5].history.data;
          const solar = x[6].history.data;
          const wind = x[7].history.data;
          const exports = x[8].history.data;
          const imports = x[9].history.data;
          const rooftop_solar = x[10].history.data;
          const demand = x[12].history.data

          const black_coal_value = black_coal[black_coal.length-1];
          const distillate_value = distillate[distillate.length-1];
          const gas_ccgt_value = gas_ccgt[gas_ccgt.length-1];
          const gas_ocgt_value = gas_ocgt[gas_ocgt.length-1];
          const hydro_value = hydro[hydro.length-1];
          const pumps_value = pumps[pumps.length-1];
          const solar_value = solar[solar.length-1];
          const wind_value = wind[wind.length-1];
          const exports_value = exports[exports.length-1];
          const imports_value = imports[imports.length-1];
          const rooftop_solar_value = rooftop_solar[rooftop_solar.length-1];

          const total = demand[demand.length-1];
          const renewables = hydro_value + solar_value + wind_value + rooftop_solar_value;
           
          const renewable_proportion = renewables/total;
          const percent = Math.round(renewable_proportion * 100);
          // const price = Math.round(x[11].history.data[x[11].history.data.length-1]/10);
         
          // Ideally we can calculate the below numbers in some way!?!?
          const avg_renewable_proportion = 0.15;
          const avg_price = 0.07;
         
          // Indicative pricing model only...
          const price = Math.round(100*(avg_price - ((renewable_proportion - avg_renewable_proportion)/3)));

                    const d = new Date();
                    const imgNo = Math.round(100*Math.random()) % 2;

                     $("#energy-msg1").text(+ percent + "% renewables right now");
                    if(percent < 14) {
                          $("#energy-msg2").text("Postpone heavy power usage");
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
                              "Tumble dry your clothes!",
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
                    $("#price").text("Bee.Energy base rate is " + price + " cents per kWh.");
      });  
  };
  
  update();
    setInterval(function() {
    update();
  }, 300000);

//  fetch('https://api.bee.energy:8000/current')
//    .then(function(response) {
//      response.text().then(function (text) {
//          const json = JSON.parse(text);
//          let price = Math.round(json.spotPrice/10);
//          const d = new Date();
//          const imgNo = Math.round(100*Math.random()) % 2;
//
//        //James change here:
//          let percent = 8;
//           $("#energy-msg1").text(+ percent + "% renewables right now");
//          if(percent < 10) {
//                $("#energy-msg2").text("Postpone heavy power usage if possible");
//                $("#background").removeClass("bg-primary");
//                $("#background").addClass("bg-danger");
//                $("#background1").removeClass("bg-primary");
//                $("#background1").addClass("bg-danger");
//                $("#avatar").attr("src","img/scared-" + imgNo + ".svg");
//          } else {
//                let r = [
//                    "Roast that chicken!",
//                    "Grill the veggies!",
//                    "Do your laundry!",
//                    "Iron your shirts!",
//                    "Dry your clothes!",
//                    "Make a cuppa!",
//                    "Vacuum the carpet!"];
//                let rMsg = r[Math.round(100*Math.random()) % r.length];
//                $("#energy-msg2").text(rMsg);
//                $("#background").addClass("bg-primary");
//                $("#background").removeClass("bg-danger");
//                $("#background1").addClass("bg-primary");
//                $("#background1").removeClass("bg-danger");
//                $("#avatar").attr("src","img/happy-" + imgNo + ".svg");
//          }
//          $("#price").text("Current energy price is " + price + " cents per kWh.");
//      });
//    });

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
