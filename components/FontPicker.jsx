import * as Font from "expo-font";
import DocumentPicker from "react-native-document-picker";
import { getCache, setCache } from "../app/context/Storage";

export async function PickFont() {
	try {
		const res = await DocumentPicker.pick({
			type: [
				// Android MIME types
				"font/ttf",
				"font/otf",
				"application/x-font-ttf",
				"application/x-font-otf",
				// iOS UTIs
				"public.truetype-ttf-font",
				"public.opentype-font",
				"public.font",
			],
			allowMultiSelection: false,
		});
		console.log(res);
		return res[0].uri;
	} catch (error) {
		if (DocumentPicker.isCancel(error)) return null;
		console.log("Font Pick Error: ", error);
	}
}

export async function LoadFont(name, uri) {
	await Font.loadAsync({
		[name]: uri,
	});
	const stored = JSON.parse(getCache("storedUserFonts") || "{}");
	stored[name] = uri;
	setCache("storedUserFonts", stored);
	return name;
}
