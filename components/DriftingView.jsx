import { useEffect, useRef, useState } from "react";
import { Animated, ImageBackground, View } from "react-native";
import { scale } from "react-native-size-scaling";
import { useUserPreferences } from "../app/context/UserPreference";

const DriftingView = ({ children, padding = scale(30), backgroundImage, styling, onLayout }) => {
    const { userPref } = useUserPreferences();
    const [boxSize, setBoxSize] = useState({ width: 0, height: 0 });
    const childSize = useRef({ width: 0, height: 0 });
    const position = useRef(new Animated.ValueXY({ x: 0, y: 0 })).current;

    // Place at center when box and child size known
    useEffect(() => {
        if (!boxSize.width || !boxSize.height || !childSize.current.width || !childSize.current.height) return;

        const centerX = (boxSize.width - (boxSize.width * 0.7)) / 2;
        const centerY = (boxSize.height - boxSize.height * 0.8) / 2;

        position.setValue({ x: centerX, y: centerY });
    }, [boxSize, position]);

    // Random drift
    useEffect(() => {
        if (!boxSize.width || !boxSize.height || !childSize.current.width || !childSize.current.height) return;
        
        const id = setInterval(() => {
            const maxX = Math.max(0, boxSize.width - childSize.current.width - padding);
            const maxY = Math.max(0, boxSize.height - childSize.current.height - padding);

            const offsetX = Math.random() * maxX;
            const offsetY = Math.random() * maxY;

            Animated.spring(position, {
                toValue: { x: offsetX, y: offsetY },
                useNativeDriver: true,
            }).start();
        }, userPref?.Randomness || 5000);

        return () => {
            clearInterval(id)
        };
    }, [boxSize, position, userPref, padding]);

    const Container = backgroundImage ? ImageBackground : View;
    const containerProps = backgroundImage
        ? { source: backgroundImage, style: { flex: 1 } }
        : { };

    return (
        <Container
            {...containerProps}
            style={{ flex: 1, backgroundColor: "#000" }}
            onLayout={(e) => {
                const { width, height } = e.nativeEvent.layout;
                setBoxSize({ width, height });
                if(onLayout) onLayout(e)
            }}
        >
            <Animated.View
                style={[{ transform: position.getTranslateTransform(), alignSelf: "flex-start" }, styling]}
                onLayout={(e) => {
                    const { width, height } = e.nativeEvent.layout;
                    childSize.current = { width, height };
                }}
            >
                {children}
            </Animated.View>
        </Container>
    );
};

export default DriftingView;
