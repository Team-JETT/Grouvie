HockeyAppToken="c581d7db745047de9bad7aa440805aa0"
AndroidAppId="046ab778662f45fe96f640e328383218"

curl \
  -F "status=2" \
  -F "notify=0" \
  -F "ipa=@app/build/outputs/apk/app-debug.apk" \
  -H "X-HockeyAppToken: $HockeyAppToken" \
  https://rink.hockeyapp.net/api/2/apps/$AndroidAppId/app_versions/upload
