//-------------------------------
// section1
//-------------------------------
const ctx = document.getElementById('section1Chart');
new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ['Character', 'City', 'Map', 'iCON', 'Furniture', 'Car', 'Place', 'Others'],
    datasets: [{
      label: 'COUNT',
      data: [
        imageCountMap.Character, imageCountMap.City, imageCountMap.Map, imageCountMap.iCON, imageCountMap.Furniture,
        imageCountMap.Car, imageCountMap.Place, imageCountMap.Others
      ],
      backgroundColor: [
        'rgba(255, 99, 132, 0.6)',    // Character - Red
        'rgba(54, 162, 235, 0.6)',    // City - Blue
        'rgba(255, 206, 86, 0.6)',    // Map - Yellow
        'rgba(75, 192, 192, 0.6)',    // iCON - Green
        'rgba(153, 102, 255, 0.6)',   // Furniture - Purple
        'rgba(255, 159, 64, 0.6)',    // Car - Orange
        'rgba(255, 99, 132, 0.6)',    // Place - Red (You can change to another color)
        'rgba(54, 162, 235, 0.6)'     // Others - Blue (You can change to another color)
      ],
      borderWidth: 1
    }]
  },
  options: {
    scales: {
      y: {
        beginAtZero: true
      }
    },
    plugins: {
      legend: {
        display: true,  // Display legend
        labels: {
          // This defines the legend labels for each dataset
          generateLabels: function(chart) {
            const data = chart.data;
            if (data.labels.length && data.datasets.length) {
              return data.labels.map(function(label, i) {
                return {
                  text: label + ': ' + data.datasets[0].data[i],  // label: value
                  fillStyle: data.datasets[0].backgroundColor[i]
                };
              });
            }
            return [];
          }
        }
      }
    }
  }
});

var swiper = new Swiper('.section1 .right .swiper', {
        slidesPerView: 1,

        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        },
});


const ctx2 = document.getElementById('section1Chart2');
new Chart(ctx2, {
  type: 'bar',
  data: {
    labels: ['Jazz', 'Rock', 'Classic', 'Progressive', 'Advertisement', 'HeavyMetal', 'Pop', 'Others'],
    datasets: [{
      label: 'COUNT',
      data: [
        musicCountMap.Jazz, musicCountMap.Rock, musicCountMap.Classic, musicCountMap.Progressive, musicCountMap.Advertisement,
        musicCountMap.HeavyMetal, musicCountMap.Pop, musicCountMap.Others
      ],
      backgroundColor: [
        'rgba(255, 99, 132, 0.6)',    // Character - Red
        'rgba(54, 162, 235, 0.6)',    // City - Blue
        'rgba(255, 206, 86, 0.6)',    // Map - Yellow
        'rgba(75, 192, 192, 0.6)',    // iCON - Green
        'rgba(153, 102, 255, 0.6)',   // Furniture - Purple
        'rgba(255, 159, 64, 0.6)',    // Car - Orange
        'rgba(255, 99, 132, 0.6)',    // Place - Red (You can change to another color)
        'rgba(54, 162, 235, 0.6)'     // Others - Blue (You can change to another color)
      ],
      borderWidth: 1
    }]
  },
  options: {
    scales: {
      y: {
        beginAtZero: true
      }
    },
    plugins: {
      legend: {
        display: true,  // Display legend
        labels: {
          // This defines the legend labels for each dataset
          generateLabels: function(chart) {
            const data = chart.data;
            if (data.labels.length && data.datasets.length) {
              return data.labels.map(function(label, i) {
                return {
                  text: label + ': ' + data.datasets[0].data[i],  // label: value
                  fillStyle: data.datasets[0].backgroundColor[i]
                };
              });
            }
            return [];
          }
        }
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
const rightBefore = document.querySelector('.section1 .right .tag');


const prev = document.querySelector('.section1 .right .swiper .swiper-button-prev');
const next = document.querySelector('.section1 .right .swiper .swiper-button-next');
prev.addEventListener('click',function(){
    console.log('click');
    rightBefore.innerHTML="IMAGE";
})
next.addEventListener('click',function(){
    console.log('click');
    const tagEl = document.querySelector('.section1 .right::before');
    rightBefore.innerHTML="MUSIC";
})

//-------------------------------
//
//-------------------------------

//-------------------------------
//
//-------------------------------

//-------------------------------
//
//-------------------------------



