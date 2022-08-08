#ifdef GL_ES
precision mediump float;
#endif

// JIZ FLOW
uniform vec2 resolution;
uniform float time;


const int   complexity      = 32;    // More points of color.
const float fluid_speed     = 100.0;  // Drives speed, higher number will make it slower.
  mat2 rotate2d(float theta) 
{
  float s = sin(theta), c = cos(theta);
  return mat2(c, -s, s, c);
}
vec3 hsv2rgb(vec3 c) {
	vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
	return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main()
{
  vec2 p=(2.0*gl_FragCoord.xy-resolution)/max(resolution.x,resolution.y);
	p*=0.85+sin(length(p*1.3)+time)*0.25;
	p.x += sin(time*0.1+p.y*3.31)*0.05;
	p.y += sin(time*0.3+p.x*14.0)*0.035;
	float vv = 0.0;
	float vv2 = 0.0;
  for(int i=1;i<complexity;i++)
  {
	  p.x += sin(length(p)+p.y)*0.2;
	  p.y += sin(p.x)*0.1;
    vec2 newp=p*.995;
    newp.x+=0.6/float(i)*sin(float(i)*p.y*1.1+time/fluid_speed*float(i+41)) + 0.5;
    newp.y+=0.6/float(i)*sin(float(i)*p.x*0.7+time/fluid_speed*float(i+14)) - 0.5;
    p=newp;
	  p.y += sin(p.x*3.0+time*0.4)*0.005;
	  p.x += sin(p.y+7.131+time)*0.006;
	  vv+=sin(p.x-p.y*5.3)*0.33;
	  vv2+=cos(p.y+p.x*(.3+float(i)*0.56))*0.05;
  }
	vec3 col = vec3(0.01/vv*5.1,0.05/vv2*1.45,vv2*4.4);
	col.b = (col.b*0.1)+col.r+.313;
	
	//col.b = 0.0;
	if (col.g > 0.0)
	{
		col.b = 0.0;
		col.r = 0.0;
	}
	float d1 = length(col);
	col = vec3(d1*0.4);
	
  gl_FragColor=vec4(col, 1.0);
}
