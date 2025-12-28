import { pick } from "@react-native-documents/picker";
import { Directory, Paths } from "expo-file-system";
import * as FileSystem from "expo-file-system/legacy";
import * as Font from "expo-font";
import { getCache, setCache } from "../app/context/Storage";

const fontDir = new Directory(Paths.document, "standByDroid-fonts");

function ensureFontDir() {
	try {
		const info = fontDir.info();
		if (!info.exists) {
			fontDir.create({ intermediates: true });
		}
	} catch (error) {
		console.log("Exist Error: ", error);
	}
}

export async function PickFont() {
	try {
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
		const res = result[0];

		ensureFontDir();

		const fileName =
			res.name.replaceAll(" ", "_").replace(/[^\w.-]/g, "") ||
			`font-${Date.now()}.ttf`;
		const destUri = `${fontDir.uri}/${fileName}`;

		await FileSystem.copyAsync({
			from: res.uri,
			to: destUri,
		});

		console.log("Font copied:", destUri);
		return destUri;
	} catch (error) {
		console.log("Font Pick Error: ", error);
	}
}

export async function LoadFont(name, uri) {
	try {
		await Font.loadAsync({
			[name]: uri,
		});
		const stored = JSON.parse(getCache("storedUserFonts") || "{}");
		stored[name] = uri;
		setCache("storedUserFonts", stored);
	} catch (error) {
		console.log("Log font error: ", error);
	}
}
