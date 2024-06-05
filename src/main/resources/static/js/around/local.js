    //참고 : https://jh-tr.tistory.com/188#google_vignette

    // Leaflet 초기화
    var map = L.map('map').setView([37.5729, 126.9794], 7);

    L.tileLayer('https://tiles.osm.kr/hot/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    // 다중 마커를 추가하고 각 마커에 팝업 설정
    var markers = [
        {latlng: [37.5729, 126.9794], popupContent: '서울특별시 종로구'},
        {latlng: [37.5631, 126.9829], popupContent: '경복궁'}
    ];

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
