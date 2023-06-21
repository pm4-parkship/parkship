import L from 'leaflet';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconMarker from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import { useEffect, useState } from 'react';
import { MapContainer, Marker, TileLayer, useMap } from 'react-leaflet';

L.Icon.Default.mergeOptions({
  iconRetinaUrl: iconRetina.src,
  iconUrl: iconMarker.src,
  shadowUrl: iconShadow.src
});

function MapController({ onChange }) {
  const map = useMap();

  useEffect(() => {
    map.on('click', (e) => {
      const { lat, lng } = e.latlng;
      onChange([lat, lng]);
    });
  }, []);

  return null;
}

export default function ParkingLotCreateMap({ onPositionChange, coords }) {
  const [position, setPosition] = useState(coords);

  useEffect(() => {
    onPositionChange(position);
  }, [position]);

  return (
    <MapContainer center={position} zoom={17} scrollWheelZoom={false}>
      <MapController onChange={setPosition} />
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={position} />
    </MapContainer>
  );
}
