const fs = require('fs');
const exec = require('util').promisify(require('child_process').exec);

const files = fs.readdirSync(__dirname).filter(f => f.endsWith('.ogg')).map(f => f.replace('.ogg', ''));

console.log(files);

async function main() {
  for (const file of (files)) {
    const pitches = [-3600, -2400, -1200, 1200, 2400, 3600];

    for (let i = 2; i < 9; i++) {
      if (i === 5) continue;

      const pitch = pitches.shift();
      const { stdout, stderr } = await exec(`sox ${file}.ogg ${file}_${i}.ogg pitch ${pitch}`);
      console.log(stdout);
    }
  }
}

main();