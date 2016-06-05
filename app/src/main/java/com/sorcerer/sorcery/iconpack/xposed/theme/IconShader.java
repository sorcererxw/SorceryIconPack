package com.sorcerer.sorcery.iconpack.xposed.theme;

public class IconShader {
/*
    static class CHANNEL {
        static final int ALPHA = 0;
        static final int BLUE = 3;
        static final int GREEN = 2;
        static final int RED = 1;

        CHANNEL() {
        }
    }

    public static class CompiledIconShader {
        static final int MAXLENGTH = 20736;
        final float[][] buffer;
        final float[] buffer_intensity;
        final float[][] icon = ((float[][]) Array.newInstance(Float.TYPE, new int[]{4, MAXLENGTH}));
        final float[] icon_intensity;
        final float[][] output = ((float[][]) Array.newInstance(Float.TYPE, new int[]{4, MAXLENGTH}));
        final float[] output_intensity;
        final int[] pixels = new int[MAXLENGTH];
        final List<Shader> shaders;
        final ShaderUses uses;

        CompiledIconShader(List<Shader> s) {
            this.shaders = s;
            this.uses = new ShaderUses(s);
            if (this.uses.buffer) {
                this.buffer = (float[][]) Array.newInstance(Float.TYPE, new int[]{4, MAXLENGTH});
            } else {
                this.buffer = (float[][]) null;
            }
            if (this.uses.icon_intensity) {
                this.icon_intensity = new float[MAXLENGTH];
            } else {
                this.icon_intensity = null;
            }
            if (this.uses.buffer_intensity) {
                this.buffer_intensity = new float[MAXLENGTH];
            } else {
                this.buffer_intensity = null;
            }
            if (this.uses.output_intensity) {
                this.output_intensity = new float[MAXLENGTH];
            } else {
                this.output_intensity = null;
            }
        }
    }

    static class IMAGE {
        static final int BUFFER = 1;
        static final int ICON = 0;
        static final int OUTPUT = 2;

        IMAGE() {
        }
    }

    static class INPUT {
        static final int AVERAGE = 0;
        static final int CHANNEL = 2;
        static final int INTENSITY = 1;
        static final int VALUE = 3;

        INPUT() {
        }
    }

    static class MODE {
        static final int ADD = 4;
        static final int DIVIDE = 3;
        static final int MULTIPLY = 2;
        static final int NONE = 0;
        static final int SUBTRACT = 5;
        static final int WRITE = 1;

        MODE() {
        }
    }

    static class Shader {
        final int input;
        final int inputChannel;
        final int inputMode;
        final float inputValue;
        final int mode;
        final int target;
        final int targetChannel;

        Shader(int mode, int target, int targetChannel, int input, int inputMode, int inputChannel, float inputValue) {
            this.mode = mode;
            this.target = target;
            this.targetChannel = targetChannel;
            this.input = input;
            this.inputMode = inputMode;
            this.inputChannel = inputChannel;
            this.inputValue = inputValue;
        }
    }

    static class ShaderUses {
        final boolean buffer;
        final boolean buffer_intensity;
        final boolean icon_intensity;
        final boolean output_intensity;

        ShaderUses(List<Shader> shaders) {
            boolean buffer = false;
            boolean icon_intensity = false;
            boolean buffer_intensity = false;
            boolean output_intensity = false;
            for (Shader s : shaders) {
                if (s.mode != 0) {
                    if (s.input == 1 || s.target == 1) {
                        buffer = true;
                    }
                    if (s.inputMode == 1) {
                        switch (s.input) {
                            case ErrorCode.NONE *//*0*//*:
                                icon_intensity = true;
                                break;
                            case TestHandler.ACTION_SHOW *//*1*//*:
                                buffer_intensity = true;
                                break;
                            case TestHandler.ACTION_HIDE *//*2*//*:
                                output_intensity = true;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            this.buffer = buffer;
            this.icon_intensity = icon_intensity;
            this.buffer_intensity = buffer_intensity;
            this.output_intensity = output_intensity;
        }
    }

    public static CompiledIconShader parseXml(XmlResourceParser xpp) {
        List<Shader> shaders = new LinkedList();
        try {
            int eventType = xpp.getEventType();
            while (eventType != 1) {
                if (eventType == 2 && xpp.getName().compareTo("exec") == 0 && xpp.getAttributeCount() == 3) {
                    Shader s = createShader(xpp.getAttributeValue(0), xpp.getAttributeValue(1), xpp.getAttributeValue(2));
                    if (s != null) {
                        shaders.add(s);
                    }
                }
                eventType = xpp.next();
            }
            return new CompiledIconShader(shaders);
        } catch (Exception e) {
            return null;
        }
    }

    private static Shader createShader(String targetStr, String modeStr, String inputStr) {
        int mode = 0;
        int target = 2;
        int targetChannel = 0;
        int input = 0;
        int inputMode = 2;
        int inputChannel = 0;
        float inputValue = 0.0f;
        try {
            switch (modeStr.charAt(0)) {
                case 'A':
                    mode = 4;
                    break;
                case 'D':
                    mode = 3;
                    break;
                case 'M':
                    mode = 2;
                    break;
                case 'S':
                    mode = 5;
                    break;
                case 'W':
                    mode = 1;
                    break;
                default:
                    throw new Exception();
            }
            switch (targetStr.charAt(0)) {
                case 'B':
                    target = 1;
                    break;
                case 'O':
                    target = 2;
                    break;
                default:
                    throw new Exception();
            }
            switch (targetStr.charAt(1)) {
                case 'A':
                    targetChannel = 0;
                    break;
                case 'B':
                    targetChannel = 3;
                    break;
                case 'G':
                    targetChannel = 2;
                    break;
                case 'R':
                    targetChannel = 1;
                    break;
                default:
                    throw new Exception();
            }
            boolean isValue = false;
            switch (inputStr.charAt(0)) {
                case 'B':
                    input = 1;
                    break;
                case 'I':
                    input = 0;
                    break;
                case 'O':
                    input = 2;
                    break;
                default:
                    inputValue = Float.parseFloat(inputStr);
                    isValue = true;
                    inputMode = 3;
                    break;
            }
            if (!isValue) {
                switch (inputStr.charAt(1)) {
                    case 'A':
                        inputChannel = 0;
                        break;
                    case 'B':
                        inputChannel = 3;
                        break;
                    case 'G':
                        inputChannel = 2;
                        break;
                    case 'H':
                        inputMode = 0;
                        break;
                    case 'I':
                        inputMode = 1;
                        break;
                    case 'R':
                        inputChannel = 1;
                        break;
                    default:
                        throw new Exception();
                }
            }
        } catch (Exception e) {
        }
        return new Shader(mode, target, targetChannel, input, inputMode, inputChannel, inputValue);
    }

    public static Drawable processIcon(Drawable icon_d, CompiledIconShader compiledShader) {
        List<Shader> shaders = compiledShader.shaders;
        if (!(icon_d instanceof BitmapDrawable)) {
            return null;
        }
        Bitmap icon_bitmap = ((BitmapDrawable) icon_d).getBitmap();
        if (icon_bitmap == null) {
            return null;
        }
        int width = icon_bitmap.getWidth();
        int height = icon_bitmap.getHeight();
        int length = width * height;
        if (length > 20736) {
            return null;
        }
        int i;
        int[] pixels = compiledShader.pixels;
        float[][] icon = compiledShader.icon;
        float[][] buffer = compiledShader.buffer;
        float[][] output = compiledShader.output;
        float icon_average = 0.0f;
        float buffer_average = 0.0f;
        float output_average = 0.0f;
        boolean icon_average_valid = false;
        boolean buffer_average_valid = false;
        boolean output_average_valid = false;
        float[] icon_intensity = compiledShader.icon_intensity;
        float[] buffer_intensity = compiledShader.buffer_intensity;
        float[] output_intensity = compiledShader.output_intensity;
        boolean icon_intensity_valid = false;
        boolean buffer_intensity_valid = false;
        boolean output_intensity_valid = false;
        icon_bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (i = 0; i < length; i++) {
            icon[3][i] = (float) (pixels[i] & 255);
            icon[2][i] = (float) ((pixels[i] >> 8) & 255);
            icon[1][i] = (float) ((pixels[i] >> 16) & 255);
            icon[0][i] = (float) ((pixels[i] >> 24) & 255);
        }
        float inputValue = 0.0f;
        float[] inputArray = null;
        float[] targetArray = null;
        for (Shader s : shaders) {
            if (s.mode != 0) {
                if (s.inputMode == 0) {
                    switch (s.input) {
                        case ErrorCode.NONE *//*0*//*:
                            if (!icon_average_valid) {
                                icon_average = getAverage(icon, length);
                                icon_average_valid = true;
                            }
                            inputValue = icon_average;
                            break;
                        case TestHandler.ACTION_SHOW *//*1*//*:
                            if (!buffer_average_valid) {
                                buffer_average = getAverage(buffer, length);
                                buffer_average_valid = true;
                            }
                            inputValue = buffer_average;
                            break;
                        case TestHandler.ACTION_HIDE *//*2*//*:
                            if (!output_average_valid) {
                                output_average = getAverage(output, length);
                                output_average_valid = true;
                            }
                            inputValue = output_average;
                            break;
                    }
                }
                if (s.inputMode == 1) {
                    switch (s.input) {
                        case ErrorCode.NONE *//*0*//*:
                            if (!icon_intensity_valid) {
                                getIntensity(icon_intensity, icon, length);
                                icon_intensity_valid = true;
                            }
                            inputArray = icon_intensity;
                            break;
                        case TestHandler.ACTION_SHOW *//*1*//*:
                            if (!buffer_intensity_valid) {
                                getIntensity(buffer_intensity, buffer, length);
                                buffer_intensity_valid = true;
                            }
                            inputArray = buffer_intensity;
                            break;
                        case TestHandler.ACTION_HIDE *//*2*//*:
                            if (!output_intensity_valid) {
                                getIntensity(output_intensity, output, length);
                                output_intensity_valid = true;
                            }
                            inputArray = output_intensity;
                            break;
                    }
                }
                if (s.inputMode == 2) {
                    switch (s.input) {
                        case ErrorCode.NONE *//*0*//*:
                            inputArray = icon[s.inputChannel];
                            break;
                        case TestHandler.ACTION_SHOW *//*1*//*:
                            inputArray = buffer[s.inputChannel];
                            break;
                        case TestHandler.ACTION_HIDE *//*2*//*:
                            inputArray = output[s.inputChannel];
                            break;
                    }
                }
                if (s.inputMode == 3) {
                    inputValue = s.inputValue;
                }
                if (s.target == 1) {
                    targetArray = buffer[s.targetChannel];
                }
                if (s.target == 2) {
                    targetArray = output[s.targetChannel];
                }
                switch (s.mode) {
                    case TestHandler.ACTION_SHOW *//*1*//*:
                        if (s.inputMode == 0 || s.inputMode == 3) {
                            Arrays.fill(targetArray, inputValue);
                        }
                        if (s.inputMode == 1 || s.inputMode == 2) {
                            System.arraycopy(inputArray, 0, targetArray, 0, length);
                            break;
                        }
                    case TestHandler.ACTION_HIDE *//*2*//*:
                        if (s.inputMode == 0 || s.inputMode == 3) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] * inputValue;
                            }
                        }
                        if (s.inputMode == 1 || s.inputMode == 2) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] * inputArray[i];
                            }
                            break;
                        }
                    case TestHandler.ACTION_DISPLAY *//*3*//*:
                        if (s.inputMode == 0 || s.inputMode == 3) {
                            inputValue = 1.0f / inputValue;
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] * inputValue;
                            }
                        }
                        if (s.inputMode == 1 || s.inputMode == 2) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] / inputArray[i];
                            }
                            break;
                        }
                    case TestHandler.ACTION_PDISPLAY *//*4*//*:
                        if (s.inputMode == 0 || s.inputMode == 3) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] + inputValue;
                            }
                        }
                        if (s.inputMode == 1 || s.inputMode == 2) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] + inputArray[i];
                            }
                            break;
                        }
                    case ErrorCode.MISSING_PERMISSION *//*5*//*:
                        if (s.inputMode == 0 || s.inputMode == 3) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] - inputValue;
                            }
                        }
                        if (s.inputMode == 1 || s.inputMode == 2) {
                            for (i = 0; i < length; i++) {
                                targetArray[i] = targetArray[i] - inputArray[i];
                            }
                            break;
                        }
                }
                switch (s.target) {
                    case TestHandler.ACTION_SHOW *//*1*//*:
                        buffer_average_valid = false;
                        buffer_intensity_valid = false;
                        break;
                    case TestHandler.ACTION_HIDE *//*2*//*:
                        output_average_valid = false;
                        output_intensity_valid = false;
                        break;
                    default:
                        break;
                }
            }
        }
        for (i = 0; i < length; i++) {
            int a = (int) output[0][i];
            int r = (int) output[1][i];
            int g = (int) output[2][i];
            int b = (int) output[3][i];
            if (a > 255) {
                a = 255;
            } else if (a < 0) {
                a = 0;
            }
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            pixels[i] = (((((a << 8) | r) << 8) | g) << 8) | b;
        }
        Bitmap output_bitmap = Bitmap.createBitmap(pixels, width, height, icon_bitmap.getConfig() == null ? Config.ARGB_8888 : icon_bitmap.getConfig());
        output_bitmap.setDensity(160);
        return new BitmapDrawable(output_bitmap);
    }

    private static float getAverage(float[][] array, int length) {
        double average = 0.0d;
        double total = 0.0d;
        for (int i = 0; i < length; i++) {
            average += (double) ((array[0][i] * ((array[1][i] + array[2][i]) + array[3][i])) / 3.0f);
            total += (double) array[0][i];
        }
        return (float) (average / total);
    }

    private static void getIntensity(float[] intensity, float[][] array, int length) {
        for (int i = 0; i < length; i++) {
            intensity[i] = ((array[1][i] + array[2][i]) + array[3][i]) / 3.0f;
        }
    }*/
}
