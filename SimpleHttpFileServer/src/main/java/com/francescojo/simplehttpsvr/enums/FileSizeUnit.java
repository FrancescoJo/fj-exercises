/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.enums;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
public enum FileSizeUnit {
    BYTES(0L, 1023L, "B"),
    KIBIBYTES(1024L, (1024L * 1024L) - 1, "KiB"),
    MEBIBYTES(1024L * 1024L, (1024L * 1024L * 1024L) - 1, "MiB"),
    GIBIBYTES(1024L * 1024L * 1024L, (1024L * 1024L * 1024L * 1024L) - 1, "GiB"),
    TEBIBYTES(1024L * 1024L * 1024L * 1024L, (1024L * 1024L * 1024L * 1024L * 1024L) - 1, "TiB");

    /*default*/ final long min;
    /*default*/ final long max;
    /*default*/ final String identifier;

    FileSizeUnit(long minimum, long maximum, String identifier) {
        this.min = minimum;
        this.max = maximum;
        this.identifier = identifier;
    }

    public static FileSizeUnit getBest(long size) {
        FileSizeUnit[] units = FileSizeUnit.values();
        for (int i = units.length - 1; i >= 0; i--) {
            FileSizeUnit unit = units[i];
            if (unit.isInRange(size)) {
                return unit;
            }
        }
        return BYTES;
    }

    /*default*/ boolean isInRange(long size) {
        return size > min && size < max;
    }

    public String asString(long size, Locale locale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyLocalizedPattern("#,###,###.##");
        double sizeDouble;
        if (0 == min) {
            sizeDouble = size;
        } else {
            sizeDouble = (double) size / min;
        }

        return df.format(sizeDouble) + " " + identifier;
    }
}
