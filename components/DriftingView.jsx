import { useEffect, useRef, useState } from "react";
import { Animated, View } from "react-native";
import { scale } from "react-native-size-scaling";
import { useUserPreferences } from "../app/context/UserPreference";

const DriftingView = ({ children, padding = scale(30) }) => {
    const { userPref } = useUserPreferences();
    const [boxSize, setBoxSize] = useState({ width: 0, height: 0 });
    const [childSize, setChildSize] = useState({ width: 0, height: 0 });
    const position = useRef(new Animated.ValueXY({ x: 0, y: 0 })).current;

    // Place at center when box and child size known
    useEffect(() => {
        if (!boxSize.width || !boxSize.height || !childSize.width || !childSize.height) return;

        const centerX = (boxSize.width - childSize.width) / 2;
        const centerY = (boxSize.height - childSize.height) / 2;

        position.setValue({ x: centerX, y: centerY });
    }, [boxSize, childSize, position]);

    // Random drift
    useEffect(() => {
        if (!boxSize.width || !boxSize.height || !childSize.width || !childSize.height) return;

        const id = setInterval(() => {
            const maxX = Math.max(0 + padding, boxSize.width - childSize.width - padding);
            const maxY = Math.max(0 + padding, boxSize.height - childSize.height - padding);

            const offsetX = Math.random() * maxX;
            const offsetY = Math.random() * maxY;

            Animated.spring(position, {
                toValue: { x: offsetX, y: offsetY },
                useNativeDriver: true,
            }).start();
        }, userPref?.Randomness || 5000);

        return () => clearInterval(id);
    }, [boxSize, childSize, position, userPref, padding]);

    return (
        <View
            style={{ flex: 1, backgroundColor: "#000" }}
            onLayout={(e) => {
                const { width, height } = e.nativeEvent.layout;
                setBoxSize({ width, height });
            }}
        >
            <Animated.View
                style={{ transform: position.getTranslateTransform(), alignSelf: "flex-start" }}
                onLayout={(e) => {
                    const { width, height } = e.nativeEvent.layout;
                    setChildSize({ width, height });
                }}
            >
                {children}
            </Animated.View>
        </View>
    );
};

export default DriftingView;
