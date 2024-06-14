//-------------------------------
// section1
//-------------------------------
const ctx = document.getElementById('section1Chart');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
        label: '# of Votes',
        data: [5, 21, 7, 4,15, 9],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });

//-------------------------------
// section2
//-------------------------------
const section2imagesBlock = new Swiper('.section2 .right .swiper', {
        loop: true,
        initialSlide: 1,
        //centeredSlides: true,
        speed: 10000,
        autoplay: {
            delay: 0,
            disableOnInteraction: false
        },
        slidesPerView: 'auto',

    });

//-------------------------------
//
//-------------------------------

//-------------------------------
//
//-------------------------------

//-------------------------------
//
//-------------------------------

//-------------------------------
//
//-------------------------------



