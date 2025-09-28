import { memo, useEffect, useState } from "react";
import { Text } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";
import { GetRandomColor } from "../../context/Randomizer";
import { useUserPreferences } from "../../context/UserPreference";

const One = memo(({ time }) => {
    const { userPref } = useUserPreferences();
    const formattedTime = time.toLocaleTimeString([], {
        hour: "numeric",
        minute: "2-digit",
        hour12: true,
    });
    const [hourMinute, ampm] = formattedTime.split("\u202F");
    const [color, setColor] = useState('#4582C0');
    const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });

    useEffect(() => {
        const interval = setInterval(() => {
            const randColor = GetRandomColor();
            setColor(randColor)
        }, userPref?.Randomness || 300000);

        return () => clearInterval(interval);
    }, []);
    
    return (
        <DriftingView 
            onLayout={(e) => {
                const { width, height } = e.nativeEvent.layout;
                setBoxSize({ width, height });
            }}
            styling={styles.container}>
            <Text
                style={[
                    styles.hourMinute,
                    {
                        color: color,
                        fontSize: Math.min(boxSize.width * 0.30, boxSize.height * 0.8),
                    },
                ]}
            >
                {hourMinute}
            </Text>
            <Text
                style={[
                    styles.ampm,
                    {
                        color: color,
                        fontSize: Math.min(boxSize.width * 0.15, boxSize.height * 0.4),
                    },
                ]}
            >
                {ampm?.toUpperCase()}
            </Text>
        </DriftingView>
    );
});

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'flex-end',
    },
    hourMinute: {
        fontSize: 90,
        fontFamily: 'transformers'
    },
    ampm: {
        fontSize: 25,
        marginLeft: 8,
        fontFamily: 'transformers'
    },
});

export default One;
