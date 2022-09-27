const instruments = ['harp', 'bass', 'flute', 'bell', 'guitar', 'chime', 'xylophone', 'iron_xylophone', 'cow_bell', 'didgeridoo', 'bit', 'banjo', 'pling', 'bd', 'snare', 'hat'];

const out = {};

for (const instrument of instruments) {
  for (let i = 2; i < 9; i++) {
    if (i === 5) continue;

    out[`${instrument}_${i}`] = { "subtitle": "subtitles.block.note_block.note", "sounds": [`nyakomod:instrument/${instrument}_${i}`] }
  }
}

console.log(JSON.stringify(out));