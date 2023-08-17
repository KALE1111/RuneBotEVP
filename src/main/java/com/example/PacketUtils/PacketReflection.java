package com.example.PacketUtils;

import com.example.Packets.BufferMethods;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PacketReflection {
    public static Class classWithgetPacketBufferNode = null;
    public static Method getPacketBufferNode = null;
    public static Class ClientPacket = null;
    public static Class isaacClass = null;
    public static Class PacketBufferNode = null;
    public static Field PACKETWRITER = null;
    public static Object isaac = null;
    public static Field mouseHandlerLastPressedTime = null;
    public static Field clientMouseLastLastPressedTimeMillis = null;


    @Inject
    Client clientInstance;
    public static Client client = null;

    @SneakyThrows
    public boolean LoadPackets() {
        try {
            client = clientInstance;
            classWithgetPacketBufferNode = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.classContainingGetPacketBufferNodeName);
            ClientPacket = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.clientPacketClassName);
            PACKETWRITER = clientInstance.getClass().getDeclaredField(ObfuscatedNames.packetWriterFieldName);
//            //Devious fix for logout issue
//            Field dc = clientInstance.getClass().getDeclaredField("dc");
//            dc.setAccessible(true);
//            dc.set(null,Integer.MAX_VALUE);
//            dc.setAccessible(false);
//            //Devious fix for logout issue
            PacketBufferNode = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferNodeClassName);

            PACKETWRITER.setAccessible(true);
            Field isaac2 = PACKETWRITER.get(null).getClass().getDeclaredField(ObfuscatedNames.isaacCipherFieldName);
            isaac2.setAccessible(true);
            isaac = isaac2.get(PACKETWRITER.get(null));
            isaac2.setAccessible(false);
            PACKETWRITER.setAccessible(false);
            isaacClass = isaac.getClass();
            getPacketBufferNode = Arrays.stream(classWithgetPacketBufferNode.getDeclaredMethods()).filter(m -> m.getReturnType().equals(PacketBufferNode)).collect(Collectors.toList()).get(0);
            mouseHandlerLastPressedTime = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass).getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);
            clientMouseLastLastPressedTimeMillis = clientInstance.getClass().getDeclaredField(ObfuscatedNames.clientMouseLastLastPressedTimeMillis);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed to load com.plugins.Packets Into Client");
            return false;
        }
        return true;
    }

//    @SneakyThrows
//    public static void writeObject(String obfname, Object buffer, Object input) {
//        switch (obfname) {
//            case "br": //ef
//                BufferMethods.br(buffer, (Integer) input);
//                break;
//            case "de":
//                BufferMethods.de(buffer, (Integer) input);
//                break;
//            case "df":
//                BufferMethods.df(buffer, (Integer) input);
//                break;
//            case "be":
//                BufferMethods.be(buffer, (Integer) input);
//                break;
//            case "dw":
//                BufferMethods.dw(buffer, (Integer) input);
//                break;
//            case "dx":
//                BufferMethods.dx(buffer, (Integer) input);
//                break;
//            case "ez":
//                BufferMethods.ez(buffer, (Integer) input);
//                break;
//            case "dz":
//                BufferMethods.dz(buffer, (Integer) input);
//                break;
//            case "bz":
//                BufferMethods.bz(buffer, (Integer) input);
//                break;
//            case "en":
//                BufferMethods.en(buffer, (Integer) input);
//                break;
//            case "dq":
//                BufferMethods.dq(buffer, (Integer) input);
//                break;
//            case "et":
//                BufferMethods.et(buffer, (Integer) input);
//                break;
//        }
//    }
@SneakyThrows
public static void writeObject(String obfname, Object buffer, Object input) {
    Method bufferMethod = BufferMethods.class.getDeclaredMethod(obfname,Object.class,int.class);
    bufferMethod.invoke(null,buffer, input);
}

    @SneakyThrows
    public static void sendPacket(PacketDef def, Object... objects) {
        Object packetBufferNode = null;
        getPacketBufferNode.setAccessible(true);
        long garbageValue = Math.abs(Long.parseLong(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        if (garbageValue < 256) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Byte.parseByte(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        } else if (garbageValue < 32768) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Short.parseShort(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        } else if (garbageValue < Integer.MAX_VALUE) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Integer.parseInt(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        }
        Object buffer = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferFieldName).get(packetBufferNode);
        getPacketBufferNode.setAccessible(false);
        List<String> params = null;
        if (def.type == PacketType.RESUME_PAUSEBUTTON) {
            params = List.of("var0", "var1");
        }
        if (def.type == PacketType.IF_BUTTON) {
            params = List.of("widgetId", "slot", "itemId");
        }
        if (def.type == PacketType.OPLOC) {
            params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
        }
        if (def.type == PacketType.OPNPC) {
            params = List.of("npcIndex", "ctrlDown");
        }
        if (def.type == PacketType.OPPLAYER) {
            params = List.of("playerIndex", "ctrlDown");
        }
        if (def.type == PacketType.OPOBJ) {
            params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
        }
        if (def.type == PacketType.OPOBJT) {
            params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
                    "ctrlDown");
        }
        if (def.type == PacketType.EVENT_MOUSE_CLICK) {
            params = List.of("mouseInfo", "mouseX", "mouseY");
        }
        if (def.type == PacketType.MOVE_GAMECLICK) {
            params = List.of("worldPointX", "worldPointY", "ctrlDown", "5");
        }
        if (def.type == PacketType.IF_BUTTONT) {
            params = List.of("sourceWidgetId", "sourceSlot", "sourceItemId", "destinationWidgetId",
                    "destinationSlot", "destinationItemId");
        }
        if (def.type == PacketType.OPLOCT) {
            params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
                    "ctrlDown");
        }
        if (def.type == PacketType.OPPLAYERT) {
            params = List.of("playerIndex", "itemId", "slot", "widgetId", "ctrlDown");
        }
        if (def.type == PacketType.OPNPCT) {
            params = List.of("npcIndex", "itemId", "slot", "widgetId", "ctrlDown");
        }
        if (params != null) {
            for (Map.Entry<String, String> stringEntry : def.fields.entrySet()) {
                if (params.contains(stringEntry.getKey())) {

                    writeObject(stringEntry.getValue(), buffer, objects[params.indexOf(stringEntry.getKey())]);
                }
            }
            PACKETWRITER.setAccessible(true);
//            Method addNode = null;
//            try {
//                for (Method declaredMethod : PACKETWRITER.get(null).getClass().getDeclaredMethods()) {
//                    int modifiers = declaredMethod.getModifiers();
//                    if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || !declaredMethod.getReturnType().equals(void.class) || declaredMethod.getParameterCount() != 2 ||
//                            !declaredMethod.getParameterTypes()[0].equals(PACKETWRITER.get(null).getClass()) || !declaredMethod.getParameterTypes()[1].equals(packetBufferNode.getClass())) {
//                        continue;
//                    }
//                    addNode = declaredMethod;
//                    addNode.setAccessible(true);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
//                if (addNode == null) {
//                    addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod(com.plugins.ObfuscatedNames.addNodeMethodName, packetBufferNode.getClass(), int.class);
//                    addNode.setAccessible(true);
//                    addNode.invoke(PACKETWRITER.get(null), packetBufferNode, Integer.parseInt(com.plugins.ObfuscatedNames.addNodeGarbageValue));
//                } else {
//                    addNode.invoke(null, PACKETWRITER.get(null), packetBufferNode);
//                }
                addNode(PACKETWRITER.get(null), packetBufferNode);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            addNode.setAccessible(false);
            PACKETWRITER.setAccessible(false);
        }
    }

    public static void addNode(Object eqVar0, Object lmVar1) {
        try {
            Field ae = eqVar0.getClass().getDeclaredField("ae");
            ae.setAccessible(true);
            Class oc = client.getClass().getClassLoader().loadClass("oc");
            Method il = oc.getDeclaredMethod("il", ae.get(eqVar0).getClass(), lmVar1.getClass().getSuperclass());
            il.setAccessible(true);
            il.invoke(null, ae.get(eqVar0), lmVar1);
//1.10.9.4 ^^ good
            Field var1at = lmVar1.getClass().getDeclaredField("at");

            //changes start here
            //Field am = lmVar1.getClass().getDeclaredField("am");
            //this changes to another field^^ not used??

            Field aoField = lmVar1.getClass().getDeclaredField("ao");
            aoField.setAccessible(true);
            Object aoObject = aoField.get(lmVar1);
            Field aaField = aoObject.getClass().getField("aa");

            //am.setAccessible(true);
            aaField.setAccessible(true);
            int aaValue = 1432958939 * aaField.getInt(aoObject);
            var1at.setInt(lmVar1, aaValue);


            aaField.setInt(aoObject, 0);

            Field var0af = eqVar0.getClass().getDeclaredField("af");
            var0af.setAccessible(true);
            var1at.setAccessible(true);
            //Field ap = eqVar0.getClass().getDeclaredField("ap");
            //ap.setAccessible(true);
            int var0afValue = var0af.getInt(eqVar0);
            int x = -111587233 * var1at.getInt(lmVar1);
            int totalAzValue = var0afValue + x;
            var0af.setInt(eqVar0, totalAzValue);
           // ap.setAccessible(false);
            var0af.setAccessible(false);
            var1at.setAccessible(false);
            //am.setAccessible(false);
            aaField.setAccessible(false);
            aoField.setAccessible(false);
            ae.setAccessible(false);
            il.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @SneakyThrows
//    static Object getPacketBufferNode(Object clientPacket, Object isaacCipher) {
//        //ClientPacket var0
//        //IsaacCipher var1
//        Object packetBufferNode = method3844();
//        Field clientPacketField = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeClientPacketField);
//        Field clientPacketClassLengthField = ClientPacket.getDeclaredField(com.plugins.ObfuscatedNames.clientPacketClassLengthField);
//        Field isaacCipherField = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeIsaacCipherField);
//        Field length = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.clientPacketLengthField);
//        Field packetBuffer = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.packetBufferFieldName);
//        Field clientPacketId = ClientPacket.getDeclaredField(com.plugins.ObfuscatedNames.clientPacketIdField);
//        Field index = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeIndexField);
//        clientPacketField.set(packetBufferNode, clientPacket);
//        int temp = length.getInt(clientPacket) * Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketLengthFieldMultiplier);
//        temp = temp * modInverse(Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketClassLengthFieldMultiplier));
//        clientPacketClassLengthField.set(packetBufferNode, temp);
//        if (-1 == clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketLengthFieldMultiplier)) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(com.plugins.ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(260);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketLengthFieldMultiplier) == -2) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(com.plugins.ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(10000);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketLengthFieldMultiplier) <= 18) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(com.plugins.ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(20);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(com.plugins.ObfuscatedNames.clientPacketLengthFieldMultiplier) <= 98) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(com.plugins.ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(100);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(com.plugins.ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(260);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        }
//        Object buffer = packetBufferNode.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferFieldName).get(packetBufferNode);
//        isaacCipherField.set(packetBufferNode, isaacCipher);
//        BufferMethods.writeByteIsaac(buffer, clientPacketId.get(clientPacket));
//        index.setInt(packetBufferNode, 0);
//        return packetBuffer;
//    }
//
//    @SneakyThrows
//    static void addNode(Object packetBufferNode) {
//        Object packetWriter = PACKETWRITER.get(null);
//        Field index = packetBufferNode.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeIndexField);
//        Field packetBuffer = packetBufferNode.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferFieldName);
//        Field packetBufferNodes = packetWriter.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodesField);
//        Field offset = packetBuffer.get(packetBufferNode).getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferOffsetField);
//        Field bufferSize = packetWriter.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetWriterBufferSizeField);
//        addFirst(packetBufferNodes.get(packetWriter), packetBufferNode);
//        index.setInt(packetBufferNode, offset.getInt(packetBuffer.get(packetBufferNode)));
//        offset.setInt(packetBuffer.get(packetBufferNode), 0);
//        bufferSize.setInt(packetBufferNode,(bufferSize.getInt(packetBufferNode)+index.getInt(packetBufferNode)));
//    }
//
//    @SneakyThrows
//    static void addFirst(Object packetBufferNodesQueue, Object packetBufferNode) {
//        if (var1.next != null) {
//            remove(var1);
//        }
//        var1.next = this.sentinel.next;
//        var1.previous = this.sentinel;
//        var1.next.previous = var1;
//        var1.previous.next = var1;
//    }
//
//    public static void remove(Object var1Node) {
//        Field next = var1Node.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeNextField);
//        Field previous = var1Node.getClass().getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodePreviousField);
//        Object nextObject= next.get(var1Node);
//        Object previousObject = previous.get(var1Node);
//        if (nextObject != null) {
//            previous.set(nextObject, previous.get(var1Node));
//            next.set(previousObject, nextObject);
//            next.set(var1Node, null);
//            previous.set(var1Node, null);
//        }
//    }
//
//    @SneakyThrows
//    static Object method3844() {
//        Field nodeCount = PacketBufferNode.getDeclaredField(com.plugins.ObfuscatedNames.packetBufferNodeCountFieldName);
//        Field nodeArray = null;
//        for (Field declaredField : PacketBufferNode.getDeclaredFields()) {
//            if (declaredField.getType().isArray() && declaredField.getType().getComponentType() == PacketBufferNode) {
//                nodeArray = declaredField;
//            }
//        }
//        if (nodeArray == null) {
//            throw new RuntimeException("Could not find nodeArray");
//        }
//        nodeCount.setAccessible(true);
//        nodeArray.setAccessible(true);
//        if (nodeCount.getInt(null) * com.plugins.ObfuscatedNames.nodeCountMultiplier == 0) {
//            nodeArray.setAccessible(false);
//            nodeCount.setAccessible(false);
//            return PacketBufferNode.newInstance();
//        } else {
//            int nodeCountValueMinusOne = (nodeCount.getInt(null) * com.plugins.ObfuscatedNames.nodeCountMultiplier) - 1;
//            int realNodeCodeValueAfterMultiplier = nodeCountValueMinusOne * modInverse(com.plugins.ObfuscatedNames.nodeCountMultiplier);
//            nodeCount.setInt(null, realNodeCodeValueAfterMultiplier);
//            Object[] nodeArrayValue = (Object[]) nodeArray.get(null);
//            nodeArray.setAccessible(false);
//            nodeCount.setAccessible(false);
//            return nodeArrayValue[realNodeCodeValueAfterMultiplier];
//        }
//    }

//    public static void doDecompilation() {
//        Runnable myRunnable =
//                new Runnable() {
//                    public void run() {
//                        File f = RuneLite.CACHE_DIR.getAbsoluteFile().toPath().resolve("patched.cache").toFile();
//                        JarFile myJar = null;
//                        try {
//                            myJar = new JarFile(f);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                        DecompilerSettings settings = new DecompilerSettings();
//                        settings.setTypeLoader(new JarTypeLoader(myJar));
//                        StringWriter output = new StringWriter();
//                        Decompiler.decompile("do", new PlainTextOutput(output), settings);
//                        String[] lines = output.toString().replaceAll("@Named\\(.*\\)", "").split("\n");
//                        List<String> methodLines = new ArrayList<>();
//                        int count = 0;
//                        boolean foundFirst = false;
//                        for (String line : lines) {
//                            if (foundFirst && count == 0) {
//                                break;
//                            }
//                            if (line.contains("static void " + com.plugins.ObfuscatedNames.RESUME_PAUSE_METHOD_NAME)) {
//                                foundFirst = true;
//                            }
//                            if (foundFirst) {
//                                if (line.contains("}")) {
//                                    count--;
//                                    continue;
//                                }
//                                if (line.contains("{")) {
//                                    count++;
//                                    continue;
//                                }
//                                if (line.contains("throw")) {
//                                    continue;
//                                }
//                                methodLines.add(line);
//                            }
//                        }
//                        for (String methodLine : methodLines) {
//                            System.out.println(methodLine);
//                        }
//                    }
//                };
//        Thread thread = new Thread(myRunnable);
//        thread.start();
//    }

    @SneakyThrows
    static Field fetchPacketField(String name) {
        return ClientPacket.getDeclaredField(name);
    }


    private static BigInteger modInverse(BigInteger val) {
        BigInteger shift = BigInteger.ONE.shiftLeft(32);
        return val.modInverse(shift);
    }

    private static int modInverse(int val) {
        return modInverse(BigInteger.valueOf(val)).intValue();
    }
}