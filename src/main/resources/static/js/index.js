

window.addEventListener('DOMContentLoaded',function(){

        //
        var section1_1 = new Swiper('.section1 .img-body-order  .swiper', {
                        slidesPerView: 5,
                        spaceBetween: 10, // 슬라이드 사이의 간격 설정
                        navigation: {
                          nextEl: '.section1 .left .img-body-order  .swiper-button-next',
                          prevEl: '.section1 .left .img-body-order  .swiper-button-prev',
                        },
        });
        var section1_2 = new Swiper('.section1 .img-body-like  .swiper', {
                        slidesPerView: 5,
                        spaceBetween: 10, // 슬라이드 사이의 간격 설정
                        navigation: {
                          nextEl: '.section1 .left .img-body-like   .swiper-button-next',
                          prevEl: '.section1 .left .img-body-like   .swiper-button-prev',
                        },
        });
        var section1_3 = new Swiper('.section1 .m-body-order  .swiper', {
                        slidesPerView: 5,
                        spaceBetween: 10, // 슬라이드 사이의 간격 설정
                        navigation: {
                          nextEl: '.section1 .left .m-body-order   .swiper-button-next',
                          prevEl: '.section1 .left .m-body-order   .swiper-button-prev',
                        },
        });
        var section1_4 = new Swiper('.section1 .m-body-like  .swiper', {
                        slidesPerView: 5,
                        spaceBetween: 10, // 슬라이드 사이의 간격 설정
                        navigation: {
                          nextEl: '.section1 .left .m-body-like   .swiper-button-next',
                          prevEl: '.section1 .left .m-body-like   .swiper-button-prev',
                        },
        });


        //이미지 좋아요/조회순 버튼
        const imageCountBtn = document.querySelector('.image-block .title-header .count-btn');
        const imageLikeBtn = document.querySelector('.image-block .title-header .like-btn');
        const imageCountOrderBlock = document.querySelector('.image-block .count-order');
        const imageLikeOrderBlock = document.querySelector('.image-block .like-order');

        imageCountBtn.addEventListener('click',function(){
            console.log('imageCountBtn clicked...');
            imageCountOrderBlock.classList.remove('hidden');
            imageLikeOrderBlock.classList.add('hidden');
        })
        imageLikeBtn.addEventListener('click',function(){
            console.log('imageLikeBtn clicked...');
            imageCountOrderBlock.classList.add('hidden');
            imageLikeOrderBlock.classList.remove('hidden');
        })


        //음악 좋아요/조회순 버튼
        const musicCountBtn = document.querySelector('.music-block .title-header .count-btn');
        const musicLikeBtn = document.querySelector('.music-block .title-header .like-btn');
        const musicCountOrderBlock = document.querySelector('.music-block .count-order');
        const musicLikeOrderBlock = document.querySelector('.music-block .like-order');

        musicCountBtn.addEventListener('click',function(){
            console.log('imageCountBtn clicked...');
            musicCountOrderBlock.classList.remove('hidden');
            musicLikeOrderBlock.classList.add('hidden');
        })
        musicLikeBtn.addEventListener('click',function(){
            console.log('imageLikeBtn clicked...');
            musicCountOrderBlock.classList.add('hidden');
            musicLikeOrderBlock.classList.remove('hidden');
        })

        //-------------------------------
        // section1
        //-------------------------------
        const ctx = document.getElementById('section1Chart');
        const section1Chart1= new Chart(ctx, {
          type: 'line',
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
                borderWidth: 1,
                borderColor:'black',
                pointRadius: 6  // 각 점의 반지름 설정
            }]
          },
          options: {
            scales: {
              y: {
                beginAtZero: true
              },
            },
            plugins: {
              legend: {
                display: false,  // Display legend
                labels: {
                  //usePointStyle: true,
                  // This defines the legend labels for each dataset
                  generateLabels: function(chart) {
                    const data = chart.data;
                    if (data.labels.length && data.datasets.length) {
                      return data.labels.map(function(label, i) {
                        return {
                            text: label + ': ' + data.datasets[0].data[i],  // label: value
                            fillStyle: data.datasets[0].backgroundColor[i],
                            //pointStyle: 'circle',  // Point style for legend
                            radius: 4  // Point radius for legend
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
                  nextEl: '.section1 .right   .swiper-button-next',
                  prevEl: '.section1 .right   .swiper-button-prev',
                },
        });


        const ctx2 = document.getElementById('section1Chart2');
        const section1Chart2=new Chart(ctx2, {
          type: 'line',
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
                borderWidth: 1,
                borderColor:'black',
                pointRadius: 6  // 각 점의 반지름 설정
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
                //display: false,  // Display legend
                labels: {
                 usePointStyle: true,
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
                nextEl: ".section2  .swiper-button-next",
                prevEl: ".section2  .swiper-button-prev"
              },

        });

        //-------------------------------
        //IMAGE / MUSIC 이동
        //-------------------------------
        const rightBefore = document.querySelector('.section1 .right .tag');
        const prev = document.querySelector('.section1 .right  .swiper-button-prev');
        const next = document.querySelector('.section1 .right  .swiper-button-next');
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


//        const btn_count = document.querySelector('.btn_count');
//        btn_count.addEventListener('click',function(){
//            imgBodyOrder.classList.remove('hidden');
//            imgBodyLike.classList.remove('hidden');
//            mBodyOrder.classList.remove('hidden');
//            mBodyLike.classList.remove('hidden');
//            if(mode==1)
//            {
//                //이미지 + 조회수만 켜기
//                imgBodyLike.classList.add('hidden');
//                mBodyOrder.classList.add('hidden');
//                mBodyLike.classList.add('hidden');
//            }else {
//                //음악 + 조회수만 켜기
//                imgBodyOrder.classList.add('hidden');
//                imgBodyLike.classList.add('hidden');
//                mBodyLike.classList.add('hidden');
//            }
//
//        })
//
//        const btn_like = document.querySelector('.btn_like');
//        btn_like.addEventListener('click',function(){
//            imgBodyOrder.classList.remove('hidden');
//            imgBodyLike.classList.remove('hidden');
//            mBodyOrder.classList.remove('hidden');
//            mBodyLike.classList.remove('hidden');
//            if(mode==1){
//                //이미지 좋아요만 켜키
//                imgBodyOrder.classList.add('hidden');
//                mBodyOrder.classList.add('hidden');
//                mBodyLike.classList.add('hidden');
//            }else {
//                //음악
//                imgBodyOrder.classList.add('hidden');
//                imgBodyLike.classList.add('hidden');
//                mBodyOrder.classList.add('hidden');
//            }
//        })

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
            //[반응형] 브라우저 크기에 따른 반응형
            //-------------------------------
            togglePagination();

            // Call togglePagination function on window resize
            window.addEventListener('resize',togglePagination);

            // Function to toggle pagination based on window size
            function togglePagination() {
                        var windowWidth = document.body.clientWidth;
                        console.log("windowWidth",windowWidth);
                        const legend1 =  section1Chart1.options.plugins.legend;
                        const legend2 =  section1Chart2.options.plugins.legend;
                        console.log(legend1.display);
                        if (windowWidth <= 450) {
                             console.log("!!!!!!!!");
                             legend1.display = false; // Hide legend
                             legend2.display = false; // Hide legend
                             section1Chart1.update();
                             section1Chart2.update();
                        } else {
                             legend1.display = true; // Hide legend
                             legend2.display = true; // Hide legend
                        }
            }

            // Initial call to togglePagination function
            togglePagination();


});




//------------------------
// 스크롤이벤트 lodash 나중에 추가할것
//------------------------
// JavaScript 코드
window.addEventListener('scroll', function() {
    var header = document.querySelector('.top-header');
    var nav = document.querySelector('nav');
    var scrollPosition = window.scrollY;
    console.log('scroll',scrollPosition);
    // header의 초기 상태 (relative)에서 fixed로 변경하기 위한 조건
    if (scrollPosition > 30) { // 스크롤이 80px 이상 내려갔을 때
        header.classList.add('fixed');
        nav.classList.add('fixed');
    } else { // 스크롤이 80px 이하로 올라갔을 때
        header.classList.remove('fixed');
        nav.classList.remove('fixed');

    }
});




