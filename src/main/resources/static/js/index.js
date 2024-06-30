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
        loop: false,
        centeredSlides: false,
        slidesPerView : 5,
        autoplay: {
            delay: 10000,
            disableOnInteraction: false
        },
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev"
      },

});

//-------------------------------
//IMAGE / MUSIC 이동
//-------------------------------
const rightBefore = document.querySelector('.section1 .right .tag');
const prev = document.querySelector('.section1 .right .swiper .swiper-button-prev');
const next = document.querySelector('.section1 .right .swiper .swiper-button-next');
const tableOrder = document.querySelector('.table.order');
const tableLike = document.querySelector('.table.like');
const title = document.querySelector('.ranking-summary .title h4');
let mode = 1;


const imgBodyOrder = document.querySelector('.img-body-order');
const imgBodyLike = document.querySelector('.img-body-like');
const mBodyOrder = document.querySelector('.m-body-order');
const mBodyLike = document.querySelector('.m-body-like');


prev.addEventListener('click',function(){
    //----------------------------------------------------------------
    imgBodyOrder.classList.remove('hidden');
    imgBodyLike.classList.remove('hidden');
    mBodyOrder.classList.remove('hidden');
    mBodyLike.classList.remove('hidden');
    //----------------------------------------------------------------
    imgBodyLike.classList.add('hidden');
    mBodyOrder.classList.add('hidden');
    mBodyLike.classList.add('hidden');
    //----------------------------------------------------------------


    console.log('click');
    rightBefore.innerHTML="IMAGE";

    tableOrder.classList.remove('hidden');
    tableLike.classList.add('hidden');
    title.innerHTML="IMAGE RANKING TOP 10";
    mode = 1;


})
next.addEventListener('click',function(){
    //----------------------------------------------------------------
    imgBodyOrder.classList.remove('hidden');
    imgBodyLike.classList.remove('hidden');
    mBodyOrder.classList.remove('hidden');
    mBodyLike.classList.remove('hidden');
    //----------------------------------------------------------------
    imgBodyOrder.classList.remove('hidden');
    imgBodyLike.classList.add('hidden');
    mBodyLike.classList.add('hidden');
    //----------------------------------------------------------------

    console.log('click');
    const tagEl = document.querySelector('.section1 .right::before');
    rightBefore.innerHTML="MUSIC";

    tableOrder.classList.add('hidden');
    tableLike.classList.remove('hidden');
    title.innerHTML="MUSIC RANKING TOP 10";
    mode = 2;
})


const btn_count = document.querySelector('.btn_count');
btn_count.addEventListener('click',function(){
    imgBodyOrder.classList.remove('hidden');
    imgBodyLike.classList.remove('hidden');
    mBodyOrder.classList.remove('hidden');
    mBodyLike.classList.remove('hidden');
    if(mode==1)
    {
        //이미지 + 조회수만 켜기
        imgBodyLike.classList.add('hidden');
        mBodyOrder.classList.add('hidden');
        mBodyLike.classList.add('hidden');
    }else {
        //음악 + 조회수만 켜기
        imgBodyOrder.classList.add('hidden');
        imgBodyLike.classList.add('hidden');
        mBodyLike.classList.add('hidden');
    }

})

const btn_like = document.querySelector('.btn_like');
btn_like.addEventListener('click',function(){
    imgBodyOrder.classList.remove('hidden');
    imgBodyLike.classList.remove('hidden');
    mBodyOrder.classList.remove('hidden');
    mBodyLike.classList.remove('hidden');
    if(mode==1){
        //이미지 좋아요만 켜키
        imgBodyOrder.classList.add('hidden');
        mBodyOrder.classList.add('hidden');
        mBodyLike.classList.add('hidden');
    }else {
        //음악
        imgBodyOrder.classList.add('hidden');
        imgBodyLike.classList.add('hidden');
        mBodyOrder.classList.add('hidden');
    }
})

//-------------------------------
// 진행중인 경매 보기
//-------------------------------
const tradingImageBtnEls = document.querySelectorAll('.tradingImageBtn');
const tradingImageModalBtn= document.querySelector('.tradingImageModalBtn');

tradingImageBtnEls.forEach(el=>{
        el.addEventListener('click',function(){
        console.log('clicked...');

        const tradingid = el.getAttribute('data-tradingid');
        const seller = el.getAttribute('data-seller');
        const buyer = el.getAttribute('data-buyer');
        const status = el.getAttribute('data-status');
        const title = el.getAttribute('data-title');
        const startPrice = el.getAttribute('data-startPrice');
        const auctionStartTime = el.getAttribute('data-auctionStartTime');
        const auctionEndTime = el.getAttribute('data-auctionEndTime');
        const price = el.getAttribute('data-price');
        tradingImageModalBtn.click();

        const modalTradingid = document.querySelector('.modal-tradingid');
        const modalStatus = document.querySelector('.modal-status');
        const modalSeller = document.querySelector('.modal-seller');
        const modalBuyer = document.querySelector('.modal-buyer');
        const modalStartPrice = document.querySelector('.modal-startPrice');
        const modalPrice = document.querySelector('.modal-price');
        const modalAuctionStartTime = document.querySelector('.modal-auctionStartTime');
        const modalAuctionEndTime = document.querySelector('.modal-auctionEndTime');

        modalTradingid.innerHTML = tradingid;
        modalStatus.innerHTML = status;
        modalSeller.innerHTML = seller;
        modalBuyer.innerHTML = buyer;
        modalStartPrice.innerHTML = (startPrice==null)?'0':startPrice + " 원";
        modalPrice.innerHTML = (price==null)?'0 원':price+ " 원";
        modalAuctionStartTime.innerHTML = auctionStartTime;
        modalAuctionEndTime.innerHTML = auctionEndTime;

        console.log("auctionEndTime",auctionEndTime);

    })
})
//-------------------------------
//
//-------------------------------
const tradingMusicBtnEls = document.querySelectorAll('.tradingMusicBtn');

tradingMusicBtnEls.forEach(el=>{

     el.addEventListener('click',function(){
        const tradingid = el.getAttribute('data-tradingid');
        const seller = el.getAttribute('data-seller');
        const buyer = el.getAttribute('data-buyer');
        const status = el.getAttribute('data-status');
        const title = el.getAttribute('data-title');
        const startPrice = el.getAttribute('data-startPrice');
        const auctionStartTime = el.getAttribute('data-auctionStartTime');
        const auctionEndTime = el.getAttribute('data-auctionEndTime');
        const price = el.getAttribute('data-price');
        tradingImageModalBtn.click();

        const modalTradingid = document.querySelector('.modal-tradingid');
        const modalStatus = document.querySelector('.modal-status');
        const modalSeller = document.querySelector('.modal-seller');
        const modalBuyer = document.querySelector('.modal-buyer');
        const modalStartPrice = document.querySelector('.modal-startPrice');
        const modalPrice = document.querySelector('.modal-price');

        const modalAuctionStartTime = document.querySelector('.modal-auctionStartTime');
        const modalAuctionEndTime = document.querySelector('.modal-auctionEndTime');

        modalTradingid.innerHTML = tradingid;
        modalStatus.innerHTML = status;
        modalSeller.innerHTML = seller;
        modalBuyer.innerHTML = buyer;
        modalStartPrice.innerHTML = (startPrice==null)?'0':startPrice + " 원";
        modalPrice.innerHTML = (price==null)?'0 원':price+ " 원";
        modalAuctionStartTime.innerHTML = auctionStartTime;
        modalAuctionEndTime.innerHTML = auctionEndTime;

     })

})

//-------------------------------
//
//-------------------------------



