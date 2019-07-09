DApp = {

    init: function() {


        fetch('http://localhost:8000/current')
          .then(function(response) {
            response.text().then(function (text) {
                const json = JSON.parse(text);
                console.log("X: " + json);
                console.log("Y: " + json.spotPrice);
                $("#cost").val(json.spotPrice);
            });

          })


//        $("#etherIn").on("paste keyup", function() {
//            $("#tokenOut").val($("#etherIn").val() * $("#buyRate").val());
//        });

    },

}

$(function() {
    DApp.init();
});
