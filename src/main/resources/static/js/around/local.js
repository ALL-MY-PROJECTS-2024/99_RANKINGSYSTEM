//----------------------------------------------------------------
// M
//----------------------------------------------------------------
    console.log(fileList);
    list = [];
    fileList.forEach(el=>{
        console.log(el);

        let popup = `<div  style='border : 1px solid gray;padding : 10px; border-radius:5px;'>
            <h6>TITLE : ${el.imagesFileInfo.images.title}</h6>
            <hr>
            <div class='m-2' style='width:200px;height:200px;'>
                <img style='width:100%;height:100%;object-fit:contain' src=${el.imagesFileInfo.dir}/${el.imagesFileInfo.filename}/>
            </div>
            <hr>
            <div class='m-2'>
                <label>CAT : </label>
                <span>${el.imagesFileInfo.images.mainCategory}</span> |
                <span>${el.imagesFileInfo.images.subCategory}</span>
            </div>
            <hr>
            <div class='m-2'>
                OWNER :  <span>${el.imagesFileInfo.images.username}</span>
            </div>
            <hr>
            <div style='display:flex;justify-content:right;'>
                <a href='javascript:void(0)' style='margin-right:5px;'>
                    <span class='material-symbols-outlined m-1'>bookmark</span>
                    <div style='font-size:.5rem;'>즐겨찾기</div>
                </a>
                <a href='javascript:void(0)'>
                    <span class='material-symbols-outlined m-1'>add</span>
                    <div style='font-size:.5rem;'>구매요청</div>
                </a>
            </div>
        </div>`;
        list.push( { latlng:[el.imagesFileInfo.images.lat,el.imagesFileInfo.images.lng],popupContent:popup });
    })
    console.log(list);
    //참고 : https://jh-tr.tistory.com/188#google_vignette

    // Leaflet 초기화
    var map = L.map('map').setView([37.5729, 126.9794], 7);

    L.tileLayer('https://tiles.osm.kr/hot/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    // 다중 마커를 추가하고 각 마커에 팝업 설정

    var markers = list;

    markers.forEach(function(markerInfo) {
        var marker = L.marker(markerInfo.latlng).addTo(map);
        marker.bindPopup(markerInfo.popupContent);
    });

    // 클릭한 위치의 정보 업데이트 함수
    function updateInfoPanel(latlng) {
        // 클릭한 위치의 위도와 경도 업데이트
        //document.getElementById('latitude').textContent = latlng.lat.toFixed(6);
        //document.getElementById('longitude').textContent = latlng.lng.toFixed(6);

        console.log('latlng',latlng);
        // 클릭한 위치의 주소를 지오코딩을 통해 가져오기(싸이트 : https://opencagedata.com/api#quickstart)
        //fetch('https://api.opencagedata.com/geocode/v1/json?q=' + latlng.lat + '+' + latlng.lng + '&key=YOUR_OPENCAGE_API_KEY')
        fetch('https://api.opencagedata.com/geocode/v1/json?q=' + latlng.lat + '+' + latlng.lng + '&key=42f328397e0144578459024d9e7c8f78')
            .then(response => response.json())
            .then(data => {
                console.log("GEOGODE..",data);
                const address = data.results[0].formatted;
                //document.getElementById('address').textContent = address;
            })
            .catch(error => {
                console.error('Error fetching address:', error);
            });
    }

    // 지도 클릭 이벤트 리스너
    map.on('click', function (e) {
        // 클릭한 지점의 정보를 사용하여 정보 패널 내용을 업데이트
        updateInfoPanel(e.latlng);
    });


//----------------------
// SWIPER
//-----------------------
       const imagesBlock = new Swiper('.images-block .swiper', {
        loop: true,
        centeredSlides: true,
        speed: 7000,
        autoplay: {
            delay: 0,
            disableOnInteraction: false
        },
        slidesPerView: 'auto',
    });
