package com.example.qr_readerexample;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.detector.Detector;

import java.util.List;
import java.util.Map;





public class QRCodeReader_597 implements Reader {
    private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
    private final Decoder decoder = new Decoder();

    public QRCodeReader_597() {
    }

    protected final Decoder getDecoder() {
        return this.decoder;
    }

    public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
        return this.decode(image, (Map)null);
    }

    public final Result decode(BinaryBitmap image, Map<DecodeHintType, ?> hints) throws NotFoundException, ChecksumException, FormatException {
        DecoderResult decoderResult;
        ResultPoint[] points;
        if(hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
            BitMatrix result1 = extractPureBits(image.getBlackMatrix());
            decoderResult = this.decoder.decode(result1, hints);
            points = NO_POINTS;
        } else {
            DetectorResult result = (new Detector(image.getBlackMatrix())).detect(hints);
            decoderResult = this.decoder.decode(result.getBits(), hints);
            points = result.getPoints();
        }

        Result result2 = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
        List byteSegments = decoderResult.getByteSegments();
        if(byteSegments != null) {
            result2.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }

        String ecLevel = decoderResult.getECLevel();
        if(ecLevel != null) {
            result2.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }

        return result2;
    }

// Xian added this mathod to check the bitMatrix
    public BitMatrix getResultAfterDectection_597(BinaryBitmap image) throws NotFoundException, FormatException {
        DetectorResult result = (new Detector(image.getBlackMatrix())).detect(null);
        BitMatrix bitMatrix=result.getBits();
        return  bitMatrix;

    }


    public void reset() {
    }

    public static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {
        int[] leftTopBlack = image.getTopLeftOnBit();
        int[] rightBottomBlack = image.getBottomRightOnBit();
        if(leftTopBlack != null && rightBottomBlack != null) {
            float moduleSize = moduleSize(leftTopBlack, image);
            int top = leftTopBlack[1];
            int bottom = rightBottomBlack[1];
            int left = leftTopBlack[0];
            int right = rightBottomBlack[0];
            if(bottom - top != right - left) {
                right = left + (bottom - top);
            }

            int matrixWidth = Math.round((float)(right - left + 1) / moduleSize);
            int matrixHeight = Math.round((float)(bottom - top + 1) / moduleSize);
            if(matrixWidth > 0 && matrixHeight > 0) {
                if(matrixHeight != matrixWidth) {
                    throw NotFoundException.getNotFoundInstance();
                } else {
                    int nudge = (int)(moduleSize / 2.0F);
                    top += nudge;
                    left += nudge;
                    BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);

                    for(int y = 0; y < matrixHeight; ++y) {
                        int iOffset = top + (int)((float)y * moduleSize);

                        for(int x = 0; x < matrixWidth; ++x) {
                            if(image.get(left + (int)((float)x * moduleSize), iOffset)) {
                                bits.set(x, y);
                            }
                        }
                    }

                    return bits;
                }
            } else {
                throw NotFoundException.getNotFoundInstance();
            }
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    private static float moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
        int height = image.getHeight();
        int width = image.getWidth();
        int x = leftTopBlack[0];
        int y = leftTopBlack[1];
        boolean inBlack = true;

        for(int transitions = 0; x < width && y < height; ++y) {
            if(inBlack != image.get(x, y)) {
                ++transitions;
                if(transitions == 5) {
                    break;
                }

                inBlack = !inBlack;
            }

            ++x;
        }

        if(x != width && y != height) {
            return (float)(x - leftTopBlack[0]) / 7.0F;
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }
}
