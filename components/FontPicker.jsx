import { pick } from "@react-native-documents/picker";
import { Directory, File } from "expo-file-system";
import * as Font from "expo-font";
import { getCache, setCache } from "../app/context/Storage";

const fontDir = new Directory(
	`${Directory.documentDirectory}standByDroid-fonts`
);

async function ensureFontDir() {
	try {
		const info = await fontDir.info();
		if (!info.exists) {
			await fontDir.create({ intermediates: true });
		}
	} catch (error) {
		console.log("Exist Error: ", error);
	}
}

export async function PickFont() {
	try {
		console.log("222222222");
		const result = await pick({
			type: [
				"font/ttf",
				"font/otf",
				"application/x-font-ttf",
				"application/x-font-otf",
				"public.truetype-ttf-font",
				"public.opentype-font",
				"public.font",
			],
			multiple: false,
		});
		console.log(result);
		const res = result[0];

		await ensureFontDir();

		const fileName = res.name || `font-${Date.now()}.ttf`;
		const destFile = fontDir.file(fileName);

		const sourceFile = new File(res.uri);
		await sourceFile.copy(destFile);
		console.log(destFile);
		return destFile.uri;
	} catch (error) {
		console.log("Font Pick Error: ", error);
	}
}

export async function LoadFont(name, uri) {
	console.log(name, uri);
	await Font.loadAsync({
		[name]: uri,
	});
	const stored = JSON.parse(getCache("storedUserFonts") || "{}");
	stored[name] = uri;
	setCache("storedUserFonts", stored);
	return name;
}
