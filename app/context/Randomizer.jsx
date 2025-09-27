export function GetRandomColor() {
  // Random hue between 0–360 for variety
  const h = Math.floor(Math.random() * 360);
  // Keep saturation reasonably high so it’s vivid
  const s = Math.floor(Math.random() * 40) + 60; // 60–100%
  // Keep lightness high so it’s visible on black
  const l = Math.floor(Math.random() * 30) + 60; // 60–90%

  return `hsl(${h}, ${s}%, ${l}%)`;
}